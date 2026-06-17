package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NewsApiProvider {

    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.url}")
    private String apiUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Mention> fetchMentions(Long brandId, String brandName) {
        List<Mention> mentions = new ArrayList<>();
        // Intento 1: en español
        mentions.addAll(fetchFromNewsApi(brandId, brandName, "es"));
        // Intento 2: sin filtro de idioma si no hay resultados
        if (mentions.isEmpty()) {
            mentions.addAll(fetchFromNewsApi(brandId, brandName, null));
        }
        return mentions;
    }

    private List<Mention> fetchFromNewsApi(Long brandId, String brandName, String language) {
        List<Mention> mentions = new ArrayList<>();
        try {
            String url = apiUrl + "?q=" + brandName.replace(" ", "+")
                    + "&sortBy=publishedAt&pageSize=10"
                    + (language != null ? "&language=" + language : "")
                    + "&apiKey=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "BrandRadar/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            log.info("NewsApiProvider - Status={} language={} for brand={}",
                    response.statusCode(), language, brandName);

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode articles = root.get("articles");
                if (articles != null && articles.isArray()) {
                    for (JsonNode article : articles) {
                        String title = article.path("title").asText("");
                        String description = article.path("description").asText("");
                        String content = title + ". " + description;
                        String url2 = article.path("url").asText("");
                        String author = article.path("author").asText("unknown");
                        String publishedAtStr = article.path("publishedAt").asText("");
                        if (content.isBlank() || content.equals("[Removed]. ")) continue;
                        Instant publishedAt = publishedAtStr.isEmpty()
                                ? Instant.now() : Instant.parse(publishedAtStr);
                        mentions.add(Mention.create(brandId, content, "NEWS", url2, author, publishedAt));
                    }
                }
                log.info("NewsApiProvider - Fetched {} mentions language={} for brand={}",
                        mentions.size(), language, brandName);
            }
        } catch (Exception e) {
            log.error("NewsApiProvider - Error for brand={}: {}", brandName, e.getMessage());
        }
        return mentions;
    }
}