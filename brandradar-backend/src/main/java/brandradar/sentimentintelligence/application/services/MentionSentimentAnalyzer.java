package brandradar.sentimentintelligence.application.services;

import brandradar.crisisdetection.infrastructure.groq.GroqApiClient;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Analiza el sentimiento de menciones NUEVAS vía Groq, en lotes de hasta 20, y devuelve
 * copias de esas menciones con el sentimiento ya calculado — listas para guardar. Se llama
 * UNA VEZ por cada tanda de ingesta (MentionIngestionService), nunca en cada carga de dashboard.
 */
@Slf4j
@Service
public class MentionSentimentAnalyzer {

    private static final int BATCH_SIZE = 20;

    private final GroqApiClient groqApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MentionSentimentAnalyzer(GroqApiClient groqApiClient) {
        this.groqApiClient = groqApiClient;
    }

    public List<Mention> analyzeAll(String brandName, List<Mention> mentions) {
        List<Mention> result = new ArrayList<>();
        for (int start = 0; start < mentions.size(); start += BATCH_SIZE) {
            int end = Math.min(start + BATCH_SIZE, mentions.size());
            result.addAll(analyzeBatch(brandName, mentions.subList(start, end)));
        }
        return result;
    }

    private List<Mention> analyzeBatch(String brandName, List<Mention> batch) {
        StringBuilder textsBuilder = new StringBuilder();
        for (int i = 0; i < batch.size(); i++) {
            textsBuilder.append(i + 1).append(". ").append(batch.get(i).getContent()).append("\n");
        }

        String prompt = String.format("""
                Analiza el sentimiento de estas menciones sobre la marca "%s".
                Para cada mención devuelve un JSON array con objetos que tengan:
                - "index": número de mención (1-based)
                - "positive": valor entre 0.0 y 1.0
                - "negative": valor entre 0.0 y 1.0
                - "neutral": valor entre 0.0 y 1.0
                - "compound": valor entre -1.0 (muy negativo) y 1.0 (muy positivo)
                - "confidence": qué tan seguro estás del análisis, entre 0.0 y 1.0

                Menciones:
                %s

                Responde SOLO con el JSON array, sin texto adicional ni markdown.
                """, brandName, textsBuilder);

        try {
            String raw = groqApiClient.chat(prompt);
            String cleaned = raw.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json", "").replaceAll("```", "").trim();
            }

            var results = objectMapper.readTree(cleaned);
            List<Mention> analyzed = new ArrayList<>();

            for (int i = 0; i < batch.size(); i++) {
                var mention = batch.get(i);
                if (i < results.size()) {
                    var r = results.get(i);
                    var positive = bd(r.path("positive").asDouble(0.0));
                    var negative = bd(r.path("negative").asDouble(0.0));
                    var neutral = bd(r.path("neutral").asDouble(0.0));
                    var compound = bd(r.path("compound").asDouble(0.0));
                    var confidence = bd(r.path("confidence").asDouble(0.5));
                    analyzed.add(mention.withSentiment(positive, negative, neutral, compound, confidence));
                } else {
                    analyzed.add(mention); // sin análisis si Groq devolvió menos items de los esperados
                }
            }
            return analyzed;
        } catch (Exception e) {
            log.error("MentionSentimentAnalyzer - Error analyzing batch: {}", e.getMessage());
            return batch; // en caso de error, se guardan sin sentimiento (quedan en 0/neutral)
        }
    }

    private BigDecimal bd(double value) {
        return BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
    }
}