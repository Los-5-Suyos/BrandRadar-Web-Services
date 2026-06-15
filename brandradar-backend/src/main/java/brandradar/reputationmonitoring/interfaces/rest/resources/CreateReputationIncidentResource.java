package brandradar.reputationmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record CreateReputationIncidentResource(
        @NotNull Long brandId,
        Long mentionStreamId,
        Integer severityLevel,
        String severityLabel
) {}