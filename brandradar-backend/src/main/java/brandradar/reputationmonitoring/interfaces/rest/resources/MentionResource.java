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
        String authorHandle,
        Instant publishedAt,
        String category,
        BigDecimal sentimentPositive,
        BigDecimal sentimentNegative,
        BigDecimal sentimentNeutral,
        BigDecimal sentimentCompound,
        BigDecimal sentimentConfidence,
        Integer engagementLikes,
        Integer engagementComments,
        Integer engagementViews,
        String status,
        Instant createdAt
) {}