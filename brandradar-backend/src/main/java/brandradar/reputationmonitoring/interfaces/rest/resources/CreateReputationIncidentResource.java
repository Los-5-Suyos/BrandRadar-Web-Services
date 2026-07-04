package brandradar.reputationmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record CreateReputationIncidentResource(
        @NotNull Long brandId,
        Long mentionStreamId,
        Long crisisAlertId,
        Integer severityLevel,
        String severityLabel,
        String title,
        String description
) {}