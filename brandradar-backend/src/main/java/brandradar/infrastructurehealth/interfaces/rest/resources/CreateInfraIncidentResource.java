package brandradar.infrastructurehealth.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInfraIncidentResource(
        @NotNull Long serviceHealthCheckId,
        @NotBlank String incidentType,
        Integer severityLevel,
        String severityLabel
) {}