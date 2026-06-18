package brandradar.crisisdetection.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class MonitoringRule {

    private final Long id;
    private final Long brandId;
    private final String name;
    private final Boolean isActive;
    private final Integer thresholdMentionVolumeLimit;
    private final BigDecimal thresholdNegativeSentimentPct;
    private final Integer thresholdTimeWindowMinutes;
    private final Integer notifCooldownMinutes;
    private final Instant createdAt;
    private final Instant updatedAt;

    private MonitoringRule(Long id, Long brandId, String name, Boolean isActive,
                           Integer thresholdMentionVolumeLimit, BigDecimal thresholdNegativeSentimentPct,
                           Integer thresholdTimeWindowMinutes, Integer notifCooldownMinutes,
                           Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "BrandId is required");
        this.name = Objects.requireNonNull(name, "Name is required");
        this.isActive = isActive != null ? isActive : true;
        this.thresholdMentionVolumeLimit = thresholdMentionVolumeLimit != null ? thresholdMentionVolumeLimit : 100;
        this.thresholdNegativeSentimentPct = thresholdNegativeSentimentPct != null ? thresholdNegativeSentimentPct : new BigDecimal("0.50");
        this.thresholdTimeWindowMinutes = thresholdTimeWindowMinutes != null ? thresholdTimeWindowMinutes : 60;
        this.notifCooldownMinutes = notifCooldownMinutes != null ? notifCooldownMinutes : 30;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MonitoringRule create(Long brandId, String name, Integer thresholdMentionVolumeLimit,
                                        BigDecimal thresholdNegativeSentimentPct, Integer thresholdTimeWindowMinutes,
                                        Integer notifCooldownMinutes) {
        return new MonitoringRule(null, brandId, name, true, thresholdMentionVolumeLimit,
                thresholdNegativeSentimentPct, thresholdTimeWindowMinutes, notifCooldownMinutes, null, null);
    }

    public static MonitoringRule rehydrate(Long id, Long brandId, String name, Boolean isActive,
                                           Integer thresholdMentionVolumeLimit, BigDecimal thresholdNegativeSentimentPct,
                                           Integer thresholdTimeWindowMinutes, Integer notifCooldownMinutes,
                                           Instant createdAt, Instant updatedAt) {
        return new MonitoringRule(id, brandId, name, isActive, thresholdMentionVolumeLimit,
                thresholdNegativeSentimentPct, thresholdTimeWindowMinutes, notifCooldownMinutes, createdAt, updatedAt);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public String getName() { return name; }
    public Boolean getIsActive() { return isActive; }
    public Integer getThresholdMentionVolumeLimit() { return thresholdMentionVolumeLimit; }
    public BigDecimal getThresholdNegativeSentimentPct() { return thresholdNegativeSentimentPct; }
    public Integer getThresholdTimeWindowMinutes() { return thresholdTimeWindowMinutes; }
    public Integer getNotifCooldownMinutes() { return notifCooldownMinutes; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}