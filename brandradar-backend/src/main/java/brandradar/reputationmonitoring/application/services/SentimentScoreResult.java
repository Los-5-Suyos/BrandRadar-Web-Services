package brandradar.reputationmonitoring.application.services;

import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentScoreLabel;

/**
 * Resultado del cálculo de reputación de un workspace.
 *
 * @param score Score de reputación ponderado, en rango 0–100.
 * @param label Clasificación de color: ROJO (< 40), AMBER (40–69), VERDE (>= 70).
 */
public record SentimentScoreResult(int score, SentimentScoreLabel label) {}
