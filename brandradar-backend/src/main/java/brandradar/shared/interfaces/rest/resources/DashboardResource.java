package brandradar.shared.interfaces.rest.resources;

import java.util.List;

public record DashboardResource(
        Long brandId,
        String brandName,
        Double sentimentScore,
        String sentimentLabel,
        Long totalMentions,
        Double positivePercent,
        Double neutralPercent,
        Double negativePercent,
        Long activeIncidents,
        String topSource,
        List<MentionSummary> recentMentions,
        String crisisAnalysis
) {
    public record MentionSummary(
            String content,
            String platform,
            String author,
            String publishedAt
    ) {}
}