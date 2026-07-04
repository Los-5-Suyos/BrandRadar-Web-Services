package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.services.ChannelMentionProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Trae posts reales que mencionan la marca, vía SociaVault (GET /reddit/search).
 * Usa sort=new (más reciente primero) para que cada refresh traiga contenido distinto.
 * "data.posts" viene como OBJETO (llaves "0","1","2"...), no como array — por eso se
 * itera con .fields() en vez de .elements(). Parseo defensivo, igual que Twitter/TikTok.
 */
@Slf4j
@Component
public class SociaVaultRedditProvider implements ChannelMentionProvider {

    private static final int MAX_MENTIONS = 20;

    @Value("${sociavault.api.key}")
    private String apiKey;

    @Value("${sociavault.api.url}")
    private String apiBaseUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getChannelType() {
        return "REDDIT";
    }

    @Override
    public List<Mention> fetchMentions(Long brandId, String brandName) {
        try {
            String query = URLEncoder.encode(brandName, StandardCharsets.UTF_8);
            String url = apiBaseUrl + "/reddit/search?query=" + query + "&sort=new&timeframe=month";

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-Key", apiKey)
                    .GET()
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("SociaVault Reddit search status={} for brand={}", response.statusCode(), brandName);

            if (response.statusCode() != 200) {
                log.warn("SociaVault Reddit search failed ({}): {}", response.statusCode(), response.body());
                return List.of();
            }

            var mentions = parsePosts(objectMapper.readTree(response.body()), brandId);
            return mentions.stream().limit(MAX_MENTIONS).toList();
        } catch (Exception e) {
            log.error("Error fetching Reddit mentions via SociaVault: {}", e.getMessage());
            return List.of();
        }
    }

    private List<Mention> parsePosts(JsonNode root, Long brandId) {
        List<Mention> mentions = new ArrayList<>();
        JsonNode posts = root.path("data").path("posts");

        Iterator<JsonNode> postIterator;
        if (posts.isArray()) {
            postIterator = posts.elements();
        } else if (posts.isObject()) {
            List<JsonNode> values = new ArrayList<>();
            var fields = posts.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                values.add(entry.getValue());
            }
            postIterator = values.iterator();
        } else {
            log.warn("SociaVault Reddit - unexpected response shape, no data.posts found");
            return mentions;
        }

        while (postIterator.hasNext()) {
            JsonNode post = postIterator.next();
            try {
                String title = post.path("title").asText("");
                String selftext = post.path("selftext").asText("");
                String content = selftext.isBlank() ? title : title + "\n" + selftext;
                if (content.isBlank()) continue;

                String author = post.path("author").asText(null);
                String subreddit = post.path("subreddit").asText(null);
                String postUrl = post.path("url").asText(null);

                int ups = post.has("ups") ? post.path("ups").asInt(0) : post.path("score").asInt(0);
                int numComments = post.path("num_comments").asInt(0);

                long createdEpochSeconds = post.has("created_utc")
                        ? post.path("created_utc").asLong(0)
                        : post.path("created").asLong(0);
                Instant publishedAt = createdEpochSeconds > 0
                        ? Instant.ofEpochSecond(createdEpochSeconds)
                        : Instant.now();

                mentions.add(Mention.createFull(
                        brandId,
                        content,
                        "REDDIT",
                        postUrl,
                        author,
                        subreddit != null ? "r/" + subreddit : null,
                        publishedAt,
                        ups,
                        numComments,
                        0
                ));
            } catch (Exception e) {
                log.debug("SociaVault Reddit - skipping malformed entry: {}", e.getMessage());
            }
        }
        return mentions;
    }
}