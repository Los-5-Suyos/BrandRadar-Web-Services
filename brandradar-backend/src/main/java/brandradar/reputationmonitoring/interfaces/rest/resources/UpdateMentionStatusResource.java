package brandradar.reputationmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record UpdateMentionStatusResource(
        @NotBlank String status
) {}