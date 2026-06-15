package brandradar.crisisdetection.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateCrisisAlertResource(
        @NotNull Long brandId,
        Long mentionStreamId,
        Long monitoringRuleId,
        Integer priorityLevel,
        String priorityLabel,
        String triggerType,
        BigDecimal triggerDeviationPct,
        BigDecimal triggerConfidence
) {}