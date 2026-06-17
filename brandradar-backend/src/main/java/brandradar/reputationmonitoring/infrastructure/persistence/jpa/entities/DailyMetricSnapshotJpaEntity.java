package brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "DailyMetricSnapshot")
public class DailyMetricSnapshotJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DMS_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "DMS_snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Column(name = "DMS_sentiment_score", nullable = false)
    private Integer sentimentScore;

    @Column(name = "DMS_sentiment_score_label", nullable = false, length = 10)
    private String sentimentScoreLabel;

    @Column(name = "DMS_total_mentions", nullable = false)
    private Integer totalMentions;

    @Column(name = "DMS_negative_count", nullable = false)
    private Integer negativeCount;

    @Column(name = "DMS_neutral_count", nullable = false)
    private Integer neutralCount;

    @Column(name = "DMS_positive_count", nullable = false)
    private Integer positiveCount;

    @Column(name = "DMS_negative_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal negativePercent;

    @Column(name = "DMS_neutral_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal neutralPercent;

    @Column(name = "DMS_positive_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal positivePercent;

    @Column(name = "DMS_variation_vs_yesterday", precision = 6, scale = 2)
    private BigDecimal variationVsYesterday;

    @Column(name = "DMS_top_source", nullable = false, length = 20)
    private String topSource;

    @Column(name = "DMS_top_source_sentiment_index", nullable = false, length = 10)
    private String topSourceSentimentIndex;

    @Column(name = "DMS_calculated_at", nullable = false)
    private Instant calculatedAt;

    public DailyMetricSnapshotJpaEntity(Long id, Long workspaceId, LocalDate snapshotDate, Integer sentimentScore,
                                        String sentimentScoreLabel, Integer totalMentions, Integer negativeCount,
                                        Integer neutralCount, Integer positiveCount, BigDecimal negativePercent,
                                        BigDecimal neutralPercent, BigDecimal positivePercent,
                                        BigDecimal variationVsYesterday, String topSource,
                                        String topSourceSentimentIndex, Instant calculatedAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.snapshotDate = snapshotDate;
        this.sentimentScore = sentimentScore;
        this.sentimentScoreLabel = sentimentScoreLabel;
        this.totalMentions = totalMentions;
        this.negativeCount = negativeCount;
        this.neutralCount = neutralCount;
        this.positiveCount = positiveCount;
        this.negativePercent = negativePercent;
        this.neutralPercent = neutralPercent;
        this.positivePercent = positivePercent;
        this.variationVsYesterday = variationVsYesterday;
        this.topSource = topSource;
        this.topSourceSentimentIndex = topSourceSentimentIndex;
        this.calculatedAt = calculatedAt;
    }
}
