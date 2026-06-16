package brandradar.reputationmonitoring.domain.model.aggregates;

import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentIndex;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentScoreLabel;
import brandradar.reputationmonitoring.domain.model.valueobjects.SourceType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class DailyMetricSnapshot {

    private final Long id;
    private final Long workspaceId;
    private final LocalDate snapshotDate;
    private final Integer sentimentScore;
    private final SentimentScoreLabel sentimentScoreLabel;
    private final Integer totalMentions;
    private final Integer negativeCount;
    private final Integer neutralCount;
    private final Integer positiveCount;
    private final BigDecimal negativePercent;
    private final BigDecimal neutralPercent;
    private final BigDecimal positivePercent;
    private final BigDecimal variationVsYesterday;
    private final SourceType topSource;
    private final SentimentIndex topSourceSentimentIndex;
    private final Instant calculatedAt;

    private DailyMetricSnapshot(Long id, Long workspaceId, LocalDate snapshotDate, Integer sentimentScore,
                                SentimentScoreLabel sentimentScoreLabel, Integer totalMentions, Integer negativeCount,
                                Integer neutralCount, Integer positiveCount, BigDecimal negativePercent,
                                BigDecimal neutralPercent, BigDecimal positivePercent, BigDecimal variationVsYesterday,
                                SourceType topSource, SentimentIndex topSourceSentimentIndex, Instant calculatedAt) {
        this.id = id;
        this.workspaceId = Objects.requireNonNull(workspaceId, "WorkspaceId is required");
        this.snapshotDate = Objects.requireNonNull(snapshotDate, "SnapshotDate is required");
        this.sentimentScore = Objects.requireNonNull(sentimentScore, "SentimentScore is required");
        this.sentimentScoreLabel = Objects.requireNonNull(sentimentScoreLabel, "SentimentScoreLabel is required");
        this.totalMentions = Objects.requireNonNull(totalMentions, "TotalMentions is required");
        this.negativeCount = Objects.requireNonNull(negativeCount, "NegativeCount is required");
        this.neutralCount = Objects.requireNonNull(neutralCount, "NeutralCount is required");
        this.positiveCount = Objects.requireNonNull(positiveCount, "PositiveCount is required");
        this.negativePercent = Objects.requireNonNull(negativePercent, "NegativePercent is required");
        this.neutralPercent = Objects.requireNonNull(neutralPercent, "NeutralPercent is required");
        this.positivePercent = Objects.requireNonNull(positivePercent, "PositivePercent is required");
        this.variationVsYesterday = variationVsYesterday;
        this.topSource = Objects.requireNonNull(topSource, "TopSource is required");
        this.topSourceSentimentIndex = Objects.requireNonNull(topSourceSentimentIndex, "TopSourceSentimentIndex is required");
        this.calculatedAt = Objects.requireNonNull(calculatedAt, "CalculatedAt is required");
    }

    public static DailyMetricSnapshot create(Long workspaceId, LocalDate snapshotDate, Integer sentimentScore,
                                              SentimentScoreLabel sentimentScoreLabel, Integer totalMentions,
                                              Integer negativeCount, Integer neutralCount, Integer positiveCount,
                                              BigDecimal negativePercent, BigDecimal neutralPercent,
                                              BigDecimal positivePercent, BigDecimal variationVsYesterday,
                                              SourceType topSource, SentimentIndex topSourceSentimentIndex,
                                              Instant calculatedAt) {
        return new DailyMetricSnapshot(null, workspaceId, snapshotDate, sentimentScore, sentimentScoreLabel,
                totalMentions, negativeCount, neutralCount, positiveCount, negativePercent, neutralPercent,
                positivePercent, variationVsYesterday, topSource, topSourceSentimentIndex, calculatedAt);
    }

    public static DailyMetricSnapshot rehydrate(Long id, Long workspaceId, LocalDate snapshotDate,
                                                 Integer sentimentScore, SentimentScoreLabel sentimentScoreLabel,
                                                 Integer totalMentions, Integer negativeCount, Integer neutralCount,
                                                 Integer positiveCount, BigDecimal negativePercent,
                                                 BigDecimal neutralPercent, BigDecimal positivePercent,
                                                 BigDecimal variationVsYesterday, SourceType topSource,
                                                 SentimentIndex topSourceSentimentIndex, Instant calculatedAt) {
        return new DailyMetricSnapshot(id, workspaceId, snapshotDate, sentimentScore, sentimentScoreLabel,
                totalMentions, negativeCount, neutralCount, positiveCount, negativePercent, neutralPercent,
                positivePercent, variationVsYesterday, topSource, topSourceSentimentIndex, calculatedAt);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public LocalDate getSnapshotDate() { return snapshotDate; }
    public Integer getSentimentScore() { return sentimentScore; }
    public SentimentScoreLabel getSentimentScoreLabel() { return sentimentScoreLabel; }
    public Integer getTotalMentions() { return totalMentions; }
    public Integer getNegativeCount() { return negativeCount; }
    public Integer getNeutralCount() { return neutralCount; }
    public Integer getPositiveCount() { return positiveCount; }
    public BigDecimal getNegativePercent() { return negativePercent; }
    public BigDecimal getNeutralPercent() { return neutralPercent; }
    public BigDecimal getPositivePercent() { return positivePercent; }
    public BigDecimal getVariationVsYesterday() { return variationVsYesterday; }
    public SourceType getTopSource() { return topSource; }
    public SentimentIndex getTopSourceSentimentIndex() { return topSourceSentimentIndex; }
    public Instant getCalculatedAt() { return calculatedAt; }
}
