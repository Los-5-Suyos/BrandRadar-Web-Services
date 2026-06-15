package brandradar.crisisdetection.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record CrisisAlertResource(
        Long id,
        Long brandId,
        Long mentionStreamId,
        Long monitoringRuleId,
        Integer priorityLevel,
        String priorityLabel,
        String status,
        String triggerType,
        BigDecimal triggerDeviationPct,
        BigDecimal triggerConfidence,
        Instant detectedAt,
        Instant acknowledgedAt
) {}