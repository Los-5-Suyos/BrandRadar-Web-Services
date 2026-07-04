package brandradar.crisisdetection.application.commands;

import java.math.BigDecimal;

public record CreateCrisisAlertCommand(
        Long brandId,
        Long mentionStreamId,
        Long monitoringRuleId,
        Integer priorityLevel,
        String priorityLabel,
        String title,
        String description,
        String triggerType,
        BigDecimal triggerDeviationPct,
        BigDecimal triggerConfidence
) {}