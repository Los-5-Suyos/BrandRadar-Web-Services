package brandradar.crisisdetection.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "CrisisAlert")
public class CrisisAlertJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CRA_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "MES_id")
    private Long mentionStreamId;

    @Column(name = "MOR_id")
    private Long monitoringRuleId;

    @Column(name = "CRA_priority_level", nullable = false)
    private Integer priorityLevel;

    @Column(name = "CRA_priority_label", length = 50)
    private String priorityLabel;

    @Column(name = "CRA_title", length = 255)
    private String title;

    @Column(name = "CRA_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "CRA_status", nullable = false, length = 20)
    private String status;

    @Column(name = "CRA_trigger_type", length = 20)
    private String triggerType;

    @Column(name = "CRA_trigger_deviation_pct")
    private BigDecimal triggerDeviationPct;

    @Column(name = "CRA_trigger_confidence")
    private BigDecimal triggerConfidence;

    @Column(name = "CRA_detected_at")
    private Instant detectedAt;

    @Column(name = "CRA_acknowledged_at")
    private Instant acknowledgedAt;

    @Column(name = "CRA_dismissed_reason", columnDefinition = "TEXT")
    private String dismissedReason;

    @Column(name = "CRA_response_time_minutes")
    private Integer responseTimeMinutes;

    public CrisisAlertJpaEntity(Long id, Long brandId, Long mentionStreamId, Long monitoringRuleId,
                                Integer priorityLevel, String priorityLabel, String title, String description,
                                String status, String triggerType, BigDecimal triggerDeviationPct,
                                BigDecimal triggerConfidence, Instant detectedAt, Instant acknowledgedAt,
                                String dismissedReason, Integer responseTimeMinutes) {
        this.id = id;
        this.brandId = brandId;
        this.mentionStreamId = mentionStreamId;
        this.monitoringRuleId = monitoringRuleId;
        this.priorityLevel = priorityLevel;
        this.priorityLabel = priorityLabel;
        this.title = title;
        this.description = description;
        this.status = status;
        this.triggerType = triggerType;
        this.triggerDeviationPct = triggerDeviationPct;
        this.triggerConfidence = triggerConfidence;
        this.detectedAt = detectedAt;
        this.acknowledgedAt = acknowledgedAt;
        this.dismissedReason = dismissedReason;
        this.responseTimeMinutes = responseTimeMinutes;
    }
}