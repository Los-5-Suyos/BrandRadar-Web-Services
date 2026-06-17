package brandradar.sentimentintelligence.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record SentimentAnalysisResource(
        Long id,
        Long brandId,
        Instant periodFrom,
        Instant periodTo,
        BigDecimal scorePositive,
        BigDecimal scoreNegative,
        BigDecimal scoreNeutral,
        BigDecimal scoreCompound,
        String trendDirection,
        BigDecimal trendMagnitude,
        BigDecimal deltaChangePct,
        Instant createdAt
) {}