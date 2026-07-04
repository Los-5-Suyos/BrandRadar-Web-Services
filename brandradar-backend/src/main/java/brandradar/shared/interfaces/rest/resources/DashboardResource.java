package brandradar.shared.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public record DashboardResource(
        Long brandId,
        String brandName,
        Double sentimentScore,
        String sentimentLabel,
        Double scoreDeltaVsYesterday,
        Long totalMentions,
        Long mentionsToday,
        Double mentionsDeltaVsYesterdayPercent,
        Double positivePercent,
        Double neutralPercent,
        Double negativePercent,
        IncidentsSummary activeIncidents,
        String topSource,
        List<MentionSummary> recentMentions,
        String crisisAnalysis,
        Instant lastUpdatedAt
) {
    public record MentionSummary(
            String content,
            String platform,
            String author,
            String publishedAt
    ) {}

    public record IncidentsSummary(
            Long count,
            List<IncidentItem> items
    ) {}

    public record IncidentItem(
            Long id,
            String title
    ) {}
}