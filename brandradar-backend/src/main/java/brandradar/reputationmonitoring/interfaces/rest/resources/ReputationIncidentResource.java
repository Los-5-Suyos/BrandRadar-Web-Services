package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record ReputationIncidentResource(
        Long id,
        Long brandId,
        Long mentionStreamId,
        Long crisisAlertId,
        Integer severityLevel,
        String severityLabel,
        String title,
        String description,
        String status,
        Integer progressPct,
        Long assignedTo,
        BigDecimal impactScore,
        String resolutionSummary,
        Instant resolvedAt,
        Instant createdAt,
        Instant updatedAt
) {}