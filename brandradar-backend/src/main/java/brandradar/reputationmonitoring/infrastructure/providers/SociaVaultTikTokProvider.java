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
import java.util.Set;

/**
 * Trae videos reales que mencionan la marca, vía SociaVault (GET /tiktok/search/keyword).
 * "data.search_item_list" viene como objeto indexado ("0","1"...), no array. Cada item
 * trae el video dentro de "aweme_info". Mismo filtro de idioma que Twitter, usando el
 * campo "desc_language" que ya viene en la respuesta.
 *
 * También expone fetchComments(url) para traer comentarios reales de un video puntual
 * bajo demanda (drill-down, 1 crédito extra por llamada — no se usa en el refresh general).
 */
@Slf4j
@Component
public class SociaVaultTikTokProvider implements ChannelMentionProvider {

    private static final int MAX_MENTIONS = 20;
    private static final Set<String> ACCEPTED_LANGUAGES = Set.of("es", "en");

    @Value("${sociavault.api.key}")
    private String apiKey;

    @Value("${sociavault.api.url}")
    private String apiBaseUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getChannelType() {
        return "TIKTOK";
    }

    @Override
    public List<Mention> fetchMentions(Long brandId, String brandName) {
        try {
            
            String query = URLEncoder.encode(brandName, StandardCharsets.UTF_8);
            String url = apiBaseUrl + "/tiktok/search/keyword?query=" + query;

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-Key", apiKey)
                    .GET()
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("SociaVault TikTok search status={} for brand={}", response.statusCode(), brandName);

            if (response.statusCode() != 200) {
                log.warn("SociaVault TikTok search failed ({}): {}", response.statusCode(), response.body());
                return List.of();
            }

            var mentions = parseVideos(objectMapper.readTree(response.body()), brandId);
            return mentions.stream().limit(MAX_MENTIONS).toList();
        } catch (Exception e) {
            log.error("Error fetching TikTok mentions via SociaVault: {}", e.getMessage());
            return List.of();
        }
    }

    private List<Mention> parseVideos(JsonNode root, Long brandId) {
        List<Mention> mentions = new ArrayList<>();
        JsonNode items = root.path("data").path("search_item_list");

        Iterator<JsonNode> itemIterator;
        if (items.isArray()) {
            itemIterator = items.elements();
        } else if (items.isObject()) {
            List<JsonNode> values = new ArrayList<>();
            var fields = items.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                values.add(entry.getValue());
            }
            itemIterator = values.iterator();
        } else {
            log.warn("SociaVault TikTok - unexpected response shape, no data.search_item_list found");
            return mentions;
        }

        int skippedByLanguage = 0;

        while (itemIterator.hasNext()) {
            JsonNode item = itemIterator.next();
            try {
                JsonNode aweme = item.path("aweme_info");
                if (aweme.isMissingNode()) continue;

                String desc = aweme.path("desc").asText("");
                if (desc.isBlank()) continue;

                String lang = aweme.path("desc_language").asText("");
                if (!ACCEPTED_LANGUAGES.contains(lang)) {
                    skippedByLanguage++;
                    continue;
                }

                String authorName = aweme.path("author").path("nickname").asText(null);
                String authorHandle = aweme.path("author").path("unique_id").asText(null);

                long createTimeSeconds = aweme.path("create_time").asLong(0);
                Instant publishedAt = createTimeSeconds > 0
                        ? Instant.ofEpochSecond(createTimeSeconds)
                        : Instant.now();

                JsonNode stats = aweme.path("statistics");
                int likes = stats.path("digg_count").asInt(0);
                int comments = stats.path("comment_count").asInt(0);
                int views = stats.path("play_count").asInt(0);

                String videoUrl = aweme.path("share_url").asText(null);

                mentions.add(Mention.createFull(
                        brandId,
                        desc,
                        "TIKTOK",
                        videoUrl,
                        authorName,
                        authorHandle != null ? "@" + authorHandle : null,
                        publishedAt,
                        likes,
                        comments,
                        views
                ));
            } catch (Exception e) {
                log.debug("SociaVault TikTok - skipping malformed entry: {}", e.getMessage());
            }
        }
        log.info("SociaVault TikTok - {} videos descartados por idioma (spam/no relevante)", skippedByLanguage);
        return mentions;
    }

    /**
     * Trae comentarios reales de un video específico (drill-down bajo demanda, 1 crédito por
     * llamada). Se usa cuando el usuario elige "ver comentarios" en un video puntual desde
     * la pantalla de Mentions — no se llama automáticamente para cada video del refresh general.
     */
    public TikTokCommentsResult fetchComments(String videoUrl) {
        try {
            String encodedUrl = URLEncoder.encode(videoUrl, StandardCharsets.UTF_8);
            String url = apiBaseUrl + "/tiktok/comments?url=" + encodedUrl;

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-Key", apiKey)
                    .GET()
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("SociaVault TikTok comments status={} for url={}", response.statusCode(), videoUrl);

            if (response.statusCode() != 200) {
                log.warn("SociaVault TikTok comments failed ({}): {}", response.statusCode(), response.body());
                return new TikTokCommentsResult(0, List.of());
            }

            return parseComments(objectMapper.readTree(response.body()));
        } catch (Exception e) {
            log.error("Error fetching TikTok comments via SociaVault: {}", e.getMessage());
            return new TikTokCommentsResult(0, List.of());
        }
    }

    private TikTokCommentsResult parseComments(JsonNode root) {
        JsonNode dataNode = root.path("data");
        int total = dataNode.path("total").asInt(0);
        JsonNode commentsNode = dataNode.path("comments");

        Iterator<JsonNode> commentIterator;
        if (commentsNode.isArray()) {
            commentIterator = commentsNode.elements();
        } else if (commentsNode.isObject()) {
            List<JsonNode> values = new ArrayList<>();
            var fields = commentsNode.fields();
            while (fields.hasNext()) {
                values.add(fields.next().getValue());
            }
            commentIterator = values.iterator();
        } else {
            return new TikTokCommentsResult(total, List.of());
        }

        List<TikTokComment> comments = new ArrayList<>();
        while (commentIterator.hasNext()) {
            JsonNode c = commentIterator.next();
            try {
                String text = c.path("text").asText("");
                if (text.isBlank()) continue;

                String authorName = c.path("user").path("nickname").asText(null);
                String authorHandle = c.path("user").path("unique_id").asText(null);
                int likes = c.path("digg_count").asInt(0);
                long createTimeSeconds = c.path("create_time").asLong(0);
                Instant publishedAt = createTimeSeconds > 0
                        ? Instant.ofEpochSecond(createTimeSeconds)
                        : null;

                comments.add(new TikTokComment(text, authorName, authorHandle, likes, publishedAt));
            } catch (Exception e) {
                log.debug("SociaVault TikTok comments - skipping malformed entry: {}", e.getMessage());
            }
        }
        return new TikTokCommentsResult(total, comments);
    }

    public record TikTokComment(String text, String authorName, String authorHandle,
                                Integer likes, Instant publishedAt) {}

    public record TikTokCommentsResult(Integer total, List<TikTokComment> comments) {}
}