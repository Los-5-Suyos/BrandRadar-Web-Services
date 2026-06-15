package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record MentionResource(
        Long id,
        Long mentionStreamId,
        Long brandId,
        String content,
        String sourcePlatform,
        String sourceUrl,
        BigDecimal sourceReliability,
        String author,
        Instant publishedAt,
        String category,
        BigDecimal sentimentPositive,
        BigDecimal sentimentNegative,
        BigDecimal sentimentNeutral,
        BigDecimal sentimentCompound,
        BigDecimal sentimentConfidence,
        Instant createdAt
) {}