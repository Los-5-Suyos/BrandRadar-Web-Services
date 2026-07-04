package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.KeywordRuleJpaEntity;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataKeywordRuleRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class YouTubeProvider implements ChannelMentionProvider {

    @Override
    public String getChannelType() {
        return "YOUTUBE";
    }

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.url}")
    private String apiUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SpringDataKeywordRuleRepository keywordRuleRepository;

    public YouTubeProvider(SpringDataKeywordRuleRepository keywordRuleRepository) {
        this.keywordRuleRepository = keywordRuleRepository;
    }

    @Override
    public List<Mention> fetchMentions(Long brandId, String brandName) {
        List<Mention> mentions = new ArrayList<>();
        try {
            String searchQuery = buildSearchQuery(brandId, brandName);

            // Paso 1: Buscar videos que mencionen la marca o alguna de sus keywords
            List<String> videoIds = searchVideos(searchQuery);
            log.info("YouTubeProvider - Found {} videos for query=[{}]", videoIds.size(), searchQuery);

            // Paso 2: Por cada video obtener comentarios reales
            for (String videoId : videoIds) {
                List<Mention> comments = fetchComments(brandId, videoId);
                mentions.addAll(comments);
                if (mentions.size() >= 20) break; // máximo 20 comentarios
            }

            log.info("YouTubeProvider - Fetched {} comments for brand={}", mentions.size(), brandName);
        } catch (Exception e) {
            log.error("YouTubeProvider - Error for brand={}: {}", brandName, e.getMessage());
        }
        return mentions;
    }

    /**
     * Combina el nombre de la marca con sus keywords de inclusión configuradas
     * (ej. "Netflix|streaming|serie|película|suscripción") usando el operador OR (|)
     * que soporta el parámetro "q" de la YouTube Data API — así se encuentran también
     * videos relevantes que no mencionen la marca literalmente por nombre.
     */
    private String buildSearchQuery(Long brandId, String brandName) {
        var keywords = keywordRuleRepository.findByBrandIdAndIsActiveTrue(brandId)
                .stream()
                .map(KeywordRuleJpaEntity::getKeyword)
                .filter(k -> !k.equalsIgnoreCase(brandName))
                .collect(Collectors.toList());

        if (keywords.isEmpty()) {
            return brandName;
        }
        return brandName + "|" + String.join("|", keywords);
    }

    private List<String> searchVideos(String searchQuery) throws Exception {
        List<String> videoIds = new ArrayList<>();
        String encoded = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
        String url = apiUrl + "/search?part=snippet&q=" + encoded
                + "&type=video&maxResults=5&relevanceLanguage=es"
                + "&key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode items = root.path("items");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String videoId = item.path("id").path("videoId").asText("");
                    if (!videoId.isBlank()) videoIds.add(videoId);
                }
            }
        } else {
            log.warn("YouTubeProvider - Search status={}", response.statusCode());
        }
        return videoIds;
    }

    private List<Mention> fetchComments(Long brandId, String videoId) throws Exception {
        List<Mention> mentions = new ArrayList<>();
        String url = apiUrl + "/commentThreads?part=snippet&videoId=" + videoId
                + "&maxResults=10&order=relevance&key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode items = root.path("items");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    JsonNode snippet = item.path("snippet")
                            .path("topLevelComment")
                            .path("snippet");

                    String text = snippet.path("textDisplay").asText("");
                    String author = snippet.path("authorDisplayName").asText("unknown");
                    String publishedAtStr = snippet.path("publishedAt").asText("");

                    if (text.isBlank()) continue;

                    Instant publishedAt = publishedAtStr.isEmpty()
                            ? Instant.now()
                            : Instant.parse(publishedAtStr);

                    String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                    mentions.add(Mention.create(
                            brandId, text, "YOUTUBE", videoUrl, author, publishedAt));
                }
            }
        } else {
            log.warn("YouTubeProvider - Comments status={} for videoId={}",
                    response.statusCode(), videoId);
        }
        return mentions;
    }
}