package brandradar.reputationmonitoring;

import brandradar.reputationmonitoring.application.services.SentimentScoreCalculator;
import brandradar.reputationmonitoring.application.services.SentimentScoreResult;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentLabel;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentScoreLabel;
import brandradar.reputationmonitoring.domain.model.valueobjects.SourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * T-26: Pruebas unitarias de SentimentScoreCalculator.
 *
 * <p>Cubre todos los casos especificados en la tarea:
 * lista vacía, lista nula, solo NEG, solo POS, distribución mixta, y verificación de pesos.</p>
 */
class SentimentScoreCalculatorTest {

    private SentimentScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SentimentScoreCalculator();
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    /**
     * Crea una mención mínima con los datos relevantes para la fórmula de cálculo.
     */
    private Mention mention(SourceType source, double sentimentScore, SentimentLabel label) {
        return Mention.create(
                1L,                              // workspaceId
                source,
                "autor_test",                    // authorName
                100,                             // authorFollowers
                "Contenido de prueba",           // content
                "https://test.com/post/1",       // url
                BigDecimal.valueOf(sentimentScore),
                label,
                Instant.now()                    // publishedAt
        );
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Lista vacía → score=0, label=ROJO")
    void calculate_emptyList_returnsZeroRojo() {
        SentimentScoreResult result = calculator.calculate(List.of());

        assertEquals(0, result.score());
        assertEquals(SentimentScoreLabel.ROJO, result.label());
    }

    @Test
    @DisplayName("Lista nula → score=0, label=ROJO")
    void calculate_nullList_returnsZeroRojo() {
        SentimentScoreResult result = calculator.calculate(null);

        assertEquals(0, result.score());
        assertEquals(SentimentScoreLabel.ROJO, result.label());
    }

    @Test
    @DisplayName("Solo menciones NEG (score ~0.12, fuente REDDIT) → score < 20, label=ROJO")
    void calculate_onlyNegativeMentions_returnsLowScoreRojo() {
        List<Mention> mentions = List.of(
                mention(SourceType.REDDIT, 0.10, SentimentLabel.NEG),
                mention(SourceType.REDDIT, 0.12, SentimentLabel.NEG),
                mention(SourceType.REDDIT, 0.14, SentimentLabel.NEG),
                mention(SourceType.REDDIT, 0.11, SentimentLabel.NEG),
                mention(SourceType.REDDIT, 0.13, SentimentLabel.NEG)
        );

        SentimentScoreResult result = calculator.calculate(mentions);

        assertTrue(result.score() < 20,
                "Score esperado < 20, obtenido: " + result.score());
        assertEquals(SentimentScoreLabel.ROJO, result.label());
    }

    @Test
    @DisplayName("Solo menciones POS (score ~0.85, fuente NEWS) → score > 70, label=VERDE")
    void calculate_onlyPositiveMentions_returnsHighScoreVerde() {
        List<Mention> mentions = List.of(
                mention(SourceType.NEWS, 0.83, SentimentLabel.POS),
                mention(SourceType.NEWS, 0.86, SentimentLabel.POS),
                mention(SourceType.NEWS, 0.84, SentimentLabel.POS),
                mention(SourceType.NEWS, 0.87, SentimentLabel.POS),
                mention(SourceType.NEWS, 0.85, SentimentLabel.POS)
        );

        SentimentScoreResult result = calculator.calculate(mentions);

        assertTrue(result.score() > 70,
                "Score esperado > 70, obtenido: " + result.score());
        assertEquals(SentimentScoreLabel.VERDE, result.label());
    }

    @Test
    @DisplayName("Distribución 32% NEG / 48% NEU / 20% POS → score en rango AMBER (40–69)")
    void calculate_mixedDistribution_returnsAmberScore() {
        // 25 menciones: 8 NEG, 12 NEU, 5 POS  ≈ 32/48/20
        List<Mention> mentions = new ArrayList<>();
        for (int i = 0; i < 8; i++)  mentions.add(mention(SourceType.TWITTER, 0.20, SentimentLabel.NEG));
        for (int i = 0; i < 12; i++) mentions.add(mention(SourceType.TWITTER, 0.52, SentimentLabel.NEU));
        for (int i = 0; i < 5; i++)  mentions.add(mention(SourceType.TWITTER, 0.82, SentimentLabel.POS));

        SentimentScoreResult result = calculator.calculate(mentions);

        assertTrue(result.score() >= 40 && result.score() <= 69,
                "Score esperado entre 40–69 (AMBER), obtenido: " + result.score());
        assertEquals(SentimentScoreLabel.AMBER, result.label());
    }

    @Test
    @DisplayName("Pesos por fuente: NEWS (1.5) pesa más que REDDIT (0.9) con igual sentimentScore")
    void calculate_sourceWeightsAffectResult() {
        // 1 mención NEWS con score alto vs 3 menciones REDDIT con score bajo
        // El peso de NEWS (1.5) debe arrastrar el resultado hacia arriba
        List<Mention> moreNewsWeight = List.of(
                mention(SourceType.NEWS,   0.90, SentimentLabel.POS),
                mention(SourceType.REDDIT, 0.10, SentimentLabel.NEG),
                mention(SourceType.REDDIT, 0.10, SentimentLabel.NEG)
        );

        // Misma cantidad, pero invertido: 1 REDDIT alto vs 3 NEWS bajo
        List<Mention> moreRedditWeight = List.of(
                mention(SourceType.REDDIT, 0.90, SentimentLabel.POS),
                mention(SourceType.NEWS,   0.10, SentimentLabel.NEG),
                mention(SourceType.NEWS,   0.10, SentimentLabel.NEG)
        );

        int scoreWithNewsHigh   = calculator.calculate(moreNewsWeight).score();
        int scoreWithRedditHigh = calculator.calculate(moreRedditWeight).score();

        // NEWS pesa 1.5 vs REDDIT 0.9 → la mención NEWS positiva debería elevar más el score
        assertTrue(scoreWithNewsHigh > scoreWithRedditHigh,
                String.format("Se esperaba que el score con NEWS alto (%d) > score con REDDIT alto (%d)",
                        scoreWithNewsHigh, scoreWithRedditHigh));
    }

    @Test
    @DisplayName("Único elemento: score propagado correctamente sin división incorrecta")
    void calculate_singleMention_returnsCorrectScore() {
        // sentimentScore=0.70, peso NEWS=1.5 → score = round(0.70/1.0 × 100) = 70 → VERDE
        List<Mention> mentions = List.of(
                mention(SourceType.NEWS, 0.70, SentimentLabel.POS)
        );

        SentimentScoreResult result = calculator.calculate(mentions);

        assertEquals(70, result.score());
        assertEquals(SentimentScoreLabel.VERDE, result.label());
    }
}
