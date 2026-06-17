package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RedditProvider {

    @Value("${reddit.api.url}")
    private String apiUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Mention> fetchMentions(Long brandId, String brandName) {
        List<Mention> mentions = new ArrayList<>();
        try {
            String url = "https://www.reddit.com/search.json?q="
                    + brandName.replace(" ", "%20")
                    + "&sort=new&limit=10&t=week";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "BrandRadar:v1.0 (by /u/brandradar)")
                    .header("Accept", "application/json")
                    .GET()
                    .build();


            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode children = root.path("data").path("children");

                if (children.isArray()) {
                    for (JsonNode child : children) {
                        JsonNode post = child.path("data");
                        String title = post.path("title").asText("");
                        String selftext = post.path("selftext").asText("");
                        String content = title + ". " + selftext;
                        String postUrl = "https://reddit.com" + post.path("permalink").asText("");
                        String author = post.path("author").asText("unknown");
                        long createdUtc = post.path("created_utc").asLong(0);

                        if (content.isBlank() || content.equals(". ")) continue;

                        Instant publishedAt = createdUtc > 0
                                ? Instant.ofEpochSecond(createdUtc)
                                : Instant.now();

                        mentions.add(Mention.create(
                                brandId, content, "REDDIT", postUrl, author, publishedAt));
                    }
                }
                log.info("RedditProvider - Fetched {} mentions for brand={}", mentions.size(), brandName);
            } else {
                log.warn("RedditProvider - Status {} for brand={}", response.statusCode(), brandName);
            }
        } catch (Exception e) {
            log.error("RedditProvider - Error fetching mentions for brand={}: {}", brandName, e.getMessage());
        }
        return mentions;
    }
}