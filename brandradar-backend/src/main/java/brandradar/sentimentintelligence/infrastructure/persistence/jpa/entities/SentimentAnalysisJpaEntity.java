package brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SentimentAnalysis")
public class SentimentAnalysisJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEA_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "SEA_period_from", nullable = false)
    private Instant periodFrom;

    @Column(name = "SEA_period_to", nullable = false)
    private Instant periodTo;

    @Column(name = "SEA_score_positive", nullable = false)
    private BigDecimal scorePositive;

    @Column(name = "SEA_score_negative", nullable = false)
    private BigDecimal scoreNegative;

    @Column(name = "SEA_score_neutral", nullable = false)
    private BigDecimal scoreNeutral;

    @Column(name = "SEA_score_compound", nullable = false)
    private BigDecimal scoreCompound;

    @Column(name = "SEA_trend_direction", length = 20)
    private String trendDirection;

    @Column(name = "SEA_trend_magnitude", nullable = false)
    private BigDecimal trendMagnitude;

    @Column(name = "SEA_delta_previous_score", nullable = false)
    private BigDecimal deltaPreviousScore;

    @Column(name = "SEA_delta_current_score", nullable = false)
    private BigDecimal deltaCurrentScore;

    @Column(name = "SEA_delta_change_pct", nullable = false)
    private BigDecimal deltaChangePct;

    @CreatedDate
    @Column(name = "SEA_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public SentimentAnalysisJpaEntity(Long id, Long brandId, Instant periodFrom, Instant periodTo, BigDecimal scorePositive, BigDecimal scoreNegative, BigDecimal scoreNeutral, BigDecimal scoreCompound, String trendDirection, BigDecimal trendMagnitude, BigDecimal deltaPreviousScore, BigDecimal deltaCurrentScore, BigDecimal deltaChangePct) {
        this.id = id;
        this.brandId = brandId;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.scorePositive = scorePositive;
        this.scoreNegative = scoreNegative;
        this.scoreNeutral = scoreNeutral;
        this.scoreCompound = scoreCompound;
        this.trendDirection = trendDirection;
        this.trendMagnitude = trendMagnitude;
        this.deltaPreviousScore = deltaPreviousScore;
        this.deltaCurrentScore = deltaCurrentScore;
        this.deltaChangePct = deltaChangePct;
    }
}