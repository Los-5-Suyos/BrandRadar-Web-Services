package brandradar.reputationmonitoring.application.services;

import java.time.LocalDate;
import java.util.List;

public record ReportData(
        String brandName,
        LocalDate periodFrom,
        LocalDate periodTo,

        String executiveSummaryText, // frase/párrafo generado por IA sobre el estado general de la reputación

        double sentimentScore,
        Double sentimentScoreDeltaPercent,
        String sentimentScoreExplanation,

        long totalMentions,
        Double mentionsDeltaPercent,
        String mentionsExplanation,

        long reachEstimate,
        Double reachDeltaPercent,
        String reachExplanation,

        List<TrendPoint> sentimentEvolution,
        List<KeywordScore> topKeywords,
        List<CriticalAccount> criticalAccounts
) {
    public record TrendPoint(LocalDate date, double sentimentScore) {}
    public record KeywordScore(String keyword, long count, double score) {}
    public record CriticalAccount(String author, String platform, long mentionsCount, double avgSentiment) {}
}