package brandradar.sentimentintelligence.application.services;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * Calcula el score agregado de una marca a partir del sentimiento YA guardado en cada
 * Mention (calculado una vez, al momento de la ingesta, por MentionSentimentAnalyzer).
 * Ya NO llama a Groq — es un cálculo local, rápido y gratis, seguro de llamar en cada
 * carga de dashboard o ciclo del scheduler.
 */
@Slf4j
@Service
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

    public BigDecimal calculateForBrand(Long brandId, String brandName, List<Mention> mentions) {
        if (mentions == null || mentions.isEmpty()) {
            log.warn("SentimentScoreCalculator - No mentions for brand={}", brandName);
            return BigDecimal.valueOf(50);
        }

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (Mention mention : mentions) {
            double compound = mention.getSentimentCompound() != null
                    ? mention.getSentimentCompound().doubleValue() : 0.0;
            double weight = SOURCE_WEIGHTS.getOrDefault(mention.getSourcePlatform(), 1.0);

            // compound (-1 a 1) → score (0 a 100)
            double score = (compound + 1.0) / 2.0 * 100.0;
            weightedSum += score * weight;
            totalWeight += weight;
        }

        double finalScore = totalWeight > 0 ? weightedSum / totalWeight : 50.0;
        BigDecimal result = BigDecimal.valueOf(finalScore).setScale(1, RoundingMode.HALF_UP);
        log.info("SentimentScoreCalculator - Final score={} for brand={} ({} mentions)",
                result, brandName, mentions.size());
        return result;
    }

    public String getLabel(BigDecimal score) {
        double val = score.doubleValue();
        if (val >= 70) return "VERDE";
        if (val >= 45) return "AMBER";
        return "ROJO";
    }
}