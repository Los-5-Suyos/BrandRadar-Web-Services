package brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "DashboardSnapshot", uniqueConstraints = @UniqueConstraint(columnNames = {"BRA_id", "DSN_date"}))
public class DashboardSnapshotJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DSN_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "DSN_date", nullable = false)
    private LocalDate date;

    @Column(name = "DSN_sentiment_score", nullable = false)
    private BigDecimal sentimentScore;

    @Column(name = "DSN_mentions_count", nullable = false)
    private Long mentionsCount;

    @Column(name = "DSN_positive_pct", nullable = false)
    private BigDecimal positivePct;

    @Column(name = "DSN_neutral_pct", nullable = false)
    private BigDecimal neutralPct;

    @Column(name = "DSN_negative_pct", nullable = false)
    private BigDecimal negativePct;

    @Column(name = "DSN_crisis_analysis", columnDefinition = "TEXT")
    private String crisisAnalysisText;

    @CreatedDate
    @Column(name = "DSN_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public DashboardSnapshotJpaEntity(Long id, Long brandId, LocalDate date, BigDecimal sentimentScore,
                                      Long mentionsCount, BigDecimal positivePct, BigDecimal neutralPct,
                                      BigDecimal negativePct, String crisisAnalysisText) {
        this.id = id;
        this.brandId = brandId;
        this.date = date;
        this.sentimentScore = sentimentScore;
        this.mentionsCount = mentionsCount;
        this.positivePct = positivePct;
        this.neutralPct = neutralPct;
        this.negativePct = negativePct;
        this.crisisAnalysisText = crisisAnalysisText;
    }
}