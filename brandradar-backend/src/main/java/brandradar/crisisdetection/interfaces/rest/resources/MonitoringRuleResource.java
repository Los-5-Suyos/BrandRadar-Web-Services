package brandradar.crisisdetection.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record MonitoringRuleResource(
        Long id,
        Long brandId,
        String name,
        Boolean isActive,
        Integer thresholdMentionVolumeLimit,
        BigDecimal thresholdNegativeSentimentPct,
        Integer thresholdTimeWindowMinutes,
        Integer notifCooldownMinutes,
        Instant createdAt,
        Instant updatedAt
) {}