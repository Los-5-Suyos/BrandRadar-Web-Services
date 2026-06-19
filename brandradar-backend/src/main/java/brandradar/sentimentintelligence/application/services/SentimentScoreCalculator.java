package brandradar.sentimentintelligence.application.services;

import brandradar.crisisdetection.infrastructure.groq.GroqApiClient;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("sentimentIntelligenceCalculator")
public class SentimentScoreCalculator {

    private static final Map<String, Double> SOURCE_WEIGHTS = Map.of(
            "NEWS", 1.5,
            "FACEBOOK", 1.3,
            "TWITTER", 1.2,
            "YOUTUBE", 1.1,
            "TIKTOK", 1.0,
            "REDDIT", 0.9,
            "INSTAGRAM", 1.0
    );

    private final GroqApiClient groqApiClient;
    private final MentionRepository mentionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SentimentScoreCalculator(GroqApiClient groqApiClient,
                                    MentionRepository mentionRepository) {
        this.groqApiClient = groqApiClient;
        this.mentionRepository = mentionRepository;
    }

    public BigDecimal calculateForBrand(Long brandId, String brandName, List<Mention> mentions) {
        if (mentions == null || mentions.isEmpty()) {
            log.warn("SentimentScoreCalculator - No mentions for brand={}", brandName);
            return BigDecimal.valueOf(50);
        }

        // Construir texto para Groq
        StringBuilder textsBuilder = new StringBuilder();
        for (int i = 0; i < Math.min(mentions.size(), 20); i++) {
            textsBuilder.append((i + 1)).append(". ").append(mentions.get(i).getContent()).append("\n");
        }

        String prompt = String.format("""
                Analiza el sentimiento de estas menciones sobre la marca "%s".
                Para cada mención devuelve un JSON array con objetos que tengan:
                - "index": número de mención (1-based)
                - "sentiment": "POSITIVE", "NEGATIVE" o "NEUTRAL"
                - "compound": valor entre -1.0 (muy negativo) y 1.0 (muy positivo)
                
                Menciones:
                %s
                
                Responde SOLO con el JSON array, sin texto adicional ni markdown.
                Ejemplo: [{"index":1,"sentiment":"NEGATIVE","compound":-0.7},{"index":2,"sentiment":"POSITIVE","compound":0.8}]
                """, brandName, textsBuilder);

        try {
            String groqResponse = groqApiClient.chat(prompt);
            log.info("SentimentScoreCalculator - Groq response received for brand={}", brandName);

            // Limpiar respuesta
            String cleaned = groqResponse.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json", "").replaceAll("```", "").trim();
            }

            var results = objectMapper.readTree(cleaned);
            double weightedSum = 0.0;
            double totalWeight = 0.0;

            for (int i = 0; i < results.size() && i < mentions.size(); i++) {
                var result = results.get(i);
                double compound = result.path("compound").asDouble(0.0);
                String platform = mentions.get(i).getSourcePlatform();
                double weight = SOURCE_WEIGHTS.getOrDefault(platform, 1.0);

                // compound (-1 a 1) → score (0 a 100)
                double score = (compound + 1.0) / 2.0 * 100.0;
                weightedSum += score * weight;
                totalWeight += weight;
            }

            double finalScore = totalWeight > 0 ? weightedSum / totalWeight : 50.0;
            BigDecimal result = BigDecimal.valueOf(finalScore).setScale(1, RoundingMode.HALF_UP);
            log.info("SentimentScoreCalculator - Final score={} for brand={}", result, brandName);
            return result;

        } catch (Exception e) {
            log.error("SentimentScoreCalculator - Error analyzing sentiment: {}", e.getMessage());
            return BigDecimal.valueOf(50);
        }
    }

    public String getLabel(BigDecimal score) {
        double val = score.doubleValue();
        if (val >= 70) return "VERDE";
        if (val >= 45) return "AMBER";
        return "ROJO";
    }
}