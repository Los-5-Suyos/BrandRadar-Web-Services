package brandradar.crisisdetection.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "MonitoringRule")
public class MonitoringRuleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOR_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "MOR_name", nullable = false, length = 255)
    private String name;

    @Column(name = "MOR_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "MOR_threshold_mention_volume_limit", nullable = false)
    private Integer thresholdMentionVolumeLimit;

    @Column(name = "MOR_threshold_negative_sentiment_pct", nullable = false)
    private BigDecimal thresholdNegativeSentimentPct;

    @Column(name = "MOR_threshold_time_window_minutes", nullable = false)
    private Integer thresholdTimeWindowMinutes;

    @Column(name = "MOR_notif_cooldown_minutes", nullable = false)
    private Integer notifCooldownMinutes;

    @CreatedDate
    @Column(name = "MOR_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "MOR_updated_at", nullable = false)
    private Instant updatedAt;

    public MonitoringRuleJpaEntity(Long id, Long brandId, String name, Boolean isActive, Integer thresholdMentionVolumeLimit, BigDecimal thresholdNegativeSentimentPct, Integer thresholdTimeWindowMinutes, Integer notifCooldownMinutes) {
        this.id = id;
        this.brandId = brandId;
        this.name = name;
        this.isActive = isActive;
        this.thresholdMentionVolumeLimit = thresholdMentionVolumeLimit;
        this.thresholdNegativeSentimentPct = thresholdNegativeSentimentPct;
        this.thresholdTimeWindowMinutes = thresholdTimeWindowMinutes;
        this.notifCooldownMinutes = notifCooldownMinutes;
    }
}