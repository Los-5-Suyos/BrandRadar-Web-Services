package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.crisisdetection.infrastructure.groq.GroqApiClient;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
public class MockMentionProvider {

    private static final String[] PLATFORMS = {"REDDIT", "TWITTER", "INSTAGRAM", "TIKTOK", "FACEBOOK", "NEWS"};
    private final GroqApiClient groqApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    public MockMentionProvider(GroqApiClient groqApiClient) {
        this.groqApiClient = groqApiClient;
    }

    public List<Mention> generateMentions(Long brandId, String brandName, int count) {
        List<Mention> mentions = new ArrayList<>();
        try {
            String prompt = String.format("""
                Genera exactamente %d comentarios variados y realistas en español sobre la marca "%s"
                como si fueran publicaciones reales de usuarios en redes sociales peruanas.
                
                Distribuye así: 32%% negativos, 48%% neutrales, 20%% positivos.
                Los comentarios deben ser naturales, coloquiales, con jerga peruana.
                Varía los temas: servicio, delivery, calidad, precio, atención, local, etc.
                
                Responde SOLO con un JSON array así:
                [
                  {"content": "texto del comentario", "platform": "REDDIT|TWITTER|INSTAGRAM|TIKTOK"},
                  ...
                ]
                Sin markdown, sin texto adicional, solo el JSON array.
                """, count, brandName);

            String response = groqApiClient.chat(prompt);

            String cleaned = response.trim()
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            JsonNode items = objectMapper.readTree(cleaned);

            if (items.isArray()) {
                for (JsonNode item : items) {
                    String content = item.path("content").asText("");
                    String platform = item.path("platform").asText(PLATFORMS[random.nextInt(PLATFORMS.length)]);

                    if (content.isBlank()) continue;

                    String fakeUrl = "https://" + platform.toLowerCase() + ".com/post/"
                            + UUID.randomUUID().toString().substring(0, 8);
                    Instant publishedAt = Instant.now().minus(random.nextInt(1440), ChronoUnit.MINUTES);

                    mentions.add(Mention.create(brandId, content, platform, fakeUrl,
                            "user_" + random.nextInt(9999), publishedAt));
                }
            }

            log.info("MockMentionProvider - Generated {} AI mentions for brand={}", mentions.size(), brandName);
        } catch (Exception e) {
            log.error("MockMentionProvider - Error generating mentions for brand={}: {}", brandName, e.getMessage());
        }
        return mentions;
    }
}