package brandradar.reputationmonitoring.application.services;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentScoreLabel;
import brandradar.reputationmonitoring.domain.model.valueobjects.SourceType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * T-26: Algoritmo central de reputación de marca.
 *
 * <p>Pondera las menciones según la credibilidad de cada fuente para calcular
 * un score de reputación 0–100 y su clasificación de color correspondiente.</p>
 *
 * <p>Fórmula: {@code score = round( Σ(sentimentScore × peso) / Σ(peso) × 100 )}</p>
 *
 * <p>Pesos por fuente:
 * NEWS=1.5, FACEBOOK=1.3, TWITTER=1.2, YOUTUBE=1.1, TIKTOK=1.0, REDDIT=0.9</p>
 *
 * <p>Clasificación:
 * <ul>
 *   <li>ROJO  → score &lt; 40</li>
 *   <li>AMBER → score 40–69</li>
 *   <li>VERDE → score ≥ 70</li>
 * </ul></p>
 */
@Component
public class SentimentScoreCalculator {

    /** Pesos de credibilidad por fuente, según especificación T-26. */
    private static final Map<SourceType, Double> SOURCE_WEIGHTS = Map.of(
            SourceType.NEWS,     1.5,
            SourceType.FACEBOOK, 1.3,
            SourceType.TWITTER,  1.2,
            SourceType.YOUTUBE,  1.1,
            SourceType.TIKTOK,   1.0,
            SourceType.REDDIT,   0.9
    );

    /** Peso por defecto para fuentes no contempladas en el mapa. */
    private static final double DEFAULT_WEIGHT = 1.0;

    /**
     * Calcula el score de reputación ponderado a partir de una lista de menciones.
     *
     * @param mentions Lista de menciones activas del workspace. Puede ser null o vacía.
     * @return {@link SentimentScoreResult} con score=0 y label=ROJO si la lista está vacía o es null.
     */
    public SentimentScoreResult calculate(List<Mention> mentions) {
        if (mentions == null || mentions.isEmpty()) {
            return new SentimentScoreResult(0, SentimentScoreLabel.ROJO);
        }

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (Mention mention : mentions) {
            double weight = SOURCE_WEIGHTS.getOrDefault(mention.getSource(), DEFAULT_WEIGHT);
            double sentimentScore = mention.getSentimentScore().doubleValue();

            weightedSum += sentimentScore * weight;
            totalWeight += weight;
        }

        // Aplicar la fórmula: round( Σ(sentimentScore × peso) / Σ(peso) × 100 )
        int score = (int) Math.round((weightedSum / totalWeight) * 100);

        // Asegurar rango válido 0–100 ante valores extremos de sentimentScore
        score = Math.max(0, Math.min(100, score));

        return new SentimentScoreResult(score, resolveLabel(score));
    }

    /**
     * Clasifica el score numérico en una etiqueta de color.
     *
     * @param score Score calculado en rango 0–100.
     * @return {@link SentimentScoreLabel} correspondiente al score.
     */
    private SentimentScoreLabel resolveLabel(int score) {
        if (score < 40) {
            return SentimentScoreLabel.ROJO;
        } else if (score <= 69) {
            return SentimentScoreLabel.AMBER;
        } else {
            return SentimentScoreLabel.VERDE;
        }
    }
}
