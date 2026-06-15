package brandradar.infrastructurehealth.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record InfraIncidentResource(
        Long id,
        Long serviceHealthCheckId,
        String incidentType,
        Integer severityLevel,
        String severityLabel,
        String status,
        BigDecimal estimatedReputationalImpact,
        Instant detectedAt,
        Instant resolvedAt
) {}