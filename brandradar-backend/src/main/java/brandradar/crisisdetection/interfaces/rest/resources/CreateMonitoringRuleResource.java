package brandradar.crisisdetection.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateMonitoringRuleResource(
        @NotNull Long brandId,
        @NotBlank String name,
        Integer thresholdMentionVolumeLimit,
        BigDecimal thresholdNegativeSentimentPct,
        Integer thresholdTimeWindowMinutes,
        Integer notifCooldownMinutes
) {}