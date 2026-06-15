package brandradar.crisisdetection.application.commands;

import java.math.BigDecimal;

public record CreateMonitoringRuleCommand(
        Long brandId,
        String name,
        Integer thresholdMentionVolumeLimit,
        BigDecimal thresholdNegativeSentimentPct,
        Integer thresholdTimeWindowMinutes,
        Integer notifCooldownMinutes
) {}