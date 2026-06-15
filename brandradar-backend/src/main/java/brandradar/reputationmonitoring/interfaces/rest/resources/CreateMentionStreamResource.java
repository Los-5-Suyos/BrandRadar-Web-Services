package brandradar.reputationmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateMentionStreamResource(
        @NotNull Long brandId,
        @NotNull Instant periodFrom,
        @NotNull Instant periodTo
) {}