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
            String encoded = URLEncoder.encode(brandName, StandardCharsets.UTF_8);
            String url = apiUrl + "/search?part=snippet&q=" + encoded
                    + "&type=video&maxResults=10&relevanceLanguage=es"
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
                        JsonNode snippet = item.path("snippet");
                        String title = snippet.path("title").asText("");
                        String description = snippet.path("description").asText("");
                        String content = title + ". " + description;
                        String videoId = item.path("id").path("videoId").asText("");
                        String channelTitle = snippet.path("channelTitle").asText("unknown");
                        String publishedAtStr = snippet.path("publishedAt").asText("");

                        if (content.isBlank() || videoId.isBlank()) continue;

                        Instant publishedAt = publishedAtStr.isEmpty()
                                ? Instant.now()
                                : Instant.parse(publishedAtStr);

                        String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                        mentions.add(Mention.create(
                                brandId, content, "YOUTUBE", videoUrl, channelTitle, publishedAt));
                    }
                }
                log.info("YouTubeProvider - Fetched {} mentions for brand={}", mentions.size(), brandName);
            } else {
                log.warn("YouTubeProvider - Status {} for brand={}", response.statusCode(), brandName);
            }
        } catch (Exception e) {
            log.error("YouTubeProvider - Error for brand={}: {}", brandName, e.getMessage());
        }
        return mentions;
    }
}