package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record ReputationIncidentResource(
        Long id,
        Long brandId,
        Long mentionStreamId,
        Integer severityLevel,
        String severityLabel,
        String status,
        Long assignedTo,
        BigDecimal impactScore,
        String resolutionSummary,
        Instant createdAt,
        Instant updatedAt
) {}