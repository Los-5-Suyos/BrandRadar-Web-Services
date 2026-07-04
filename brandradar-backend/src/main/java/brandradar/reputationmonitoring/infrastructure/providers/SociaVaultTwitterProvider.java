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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Trae tweets reales que mencionan la marca, vía SociaVault (GET /twitter/search).
 * Usa type=Latest (más reciente primero) para que cada refresh traiga contenido distinto
 * y actualizado — los duplicados entre refreshes se filtran solos por sourceUrl en
 * MentionIngestionService. El filtro de idioma (es/en) es el que se encarga de descartar
 * el spam, en vez de depender de "Top" por relevancia.
 */
@Slf4j
@Component
public class SociaVaultTwitterProvider implements ChannelMentionProvider {

    private static final int MAX_MENTIONS = 20;
    private static final Set<String> ACCEPTED_LANGUAGES = Set.of("es", "en");
    private static final DateTimeFormatter TWITTER_DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

    @Value("${sociavault.api.key}")
    private String apiKey;

    @Value("${sociavault.api.url}")
    private String apiBaseUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getChannelType() {
        return "TWITTER";
    }

    @Override
    public List<Mention> fetchMentions(Long brandId, String brandName) {
        try {
            String query = URLEncoder.encode(brandName, StandardCharsets.UTF_8);
            String url = apiBaseUrl + "/twitter/search?query=" + query + "&type=Latest";

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-API-Key", apiKey)
                    .GET()
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("SociaVault Twitter search status={} for brand={}", response.statusCode(), brandName);

            if (response.statusCode() != 200) {
                log.warn("SociaVault Twitter search failed ({}): {}", response.statusCode(), response.body());
                return List.of();
            }

            var mentions = parseTweets(objectMapper.readTree(response.body()), brandId);
            return mentions.stream().limit(MAX_MENTIONS).toList();
        } catch (Exception e) {
            log.error("Error fetching Twitter mentions via SociaVault: {}", e.getMessage());
            return List.of();
        }
    }

    private List<Mention> parseTweets(JsonNode root, Long brandId) {
        List<Mention> mentions = new ArrayList<>();
        JsonNode instructions = root.path("data").path("result").path("timeline").path("instructions");
        if (!instructions.isArray()) {
            log.warn("SociaVault Twitter - unexpected response shape, no instructions[] found");
            return mentions;
        }

        int skippedByLanguage = 0;

        for (JsonNode instruction : instructions) {
            JsonNode entries = instruction.path("entries");
            if (!entries.isArray()) continue;

            for (JsonNode entry : entries) {
                try {
                    JsonNode tweetResult = entry.path("content").path("itemContent")
                            .path("tweet_results").path("result");
                    JsonNode legacy = tweetResult.path("legacy");
                    String text = legacy.path("full_text").asText("");
                    if (text.isBlank()) continue; // no es un tweet (carrusel de personas, cursor, etc.)

                    String lang = legacy.path("lang").asText("");
                    if (!ACCEPTED_LANGUAGES.contains(lang)) {
                        skippedByLanguage++;
                        continue;
                    }

                    String tweetId = tweetResult.path("rest_id").asText(null);
                    String screenName = tweetResult.path("core").path("user_results").path("result")
                            .path("legacy").path("screen_name").asText(null);
                    String authorName = tweetResult.path("core").path("user_results").path("result")
                            .path("legacy").path("name").asText(screenName);

                    Instant publishedAt = parseTwitterDate(legacy.path("created_at").asText(null));

                    int likes = legacy.path("favorite_count").asInt(0);
                    int replies = legacy.path("reply_count").asInt(0);
                    int views = tweetResult.path("views").path("count").asInt(0);

                    String sourceUrl = (screenName != null && tweetId != null)
                            ? "https://x.com/" + screenName + "/status/" + tweetId
                            : null;

                    mentions.add(Mention.createFull(
                            brandId,
                            text,
                            "TWITTER",
                            sourceUrl,
                            authorName,
                            screenName != null ? "@" + screenName : null,
                            publishedAt,
                            likes,
                            replies,
                            views
                    ));
                } catch (Exception e) {
                    log.debug("SociaVault Twitter - skipping malformed entry: {}", e.getMessage());
                }
            }
        }
        log.info("SociaVault Twitter - {} tweets descartados por idioma (spam/no relevante)", skippedByLanguage);
        return mentions;
    }

    private Instant parseTwitterDate(String raw) {
        if (raw == null) return Instant.now();
        try {
            return ZonedDateTime.parse(raw, TWITTER_DATE_FORMAT).toInstant();
        } catch (Exception e) {
            return Instant.now();
        }
    }
}