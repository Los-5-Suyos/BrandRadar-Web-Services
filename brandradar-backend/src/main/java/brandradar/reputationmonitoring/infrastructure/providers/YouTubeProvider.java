package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
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

@Slf4j
@Component
public class YouTubeProvider {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.url}")
    private String apiUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Mention> fetchMentions(Long brandId, String brandName) {
        List<Mention> mentions = new ArrayList<>();
        try {
            // Paso 1: Buscar videos que mencionen la marca
            List<String> videoIds = searchVideos(brandName);
            log.info("YouTubeProvider - Found {} videos for brand={}", videoIds.size(), brandName);

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

    private List<String> searchVideos(String brandName) throws Exception {
        List<String> videoIds = new ArrayList<>();
        String encoded = URLEncoder.encode(brandName, StandardCharsets.UTF_8);
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