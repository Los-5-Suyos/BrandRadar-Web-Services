package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.time.Instant;

public record MentionStreamResource(
        Long id,
        Long brandId,
        Instant periodFrom,
        Instant periodTo,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}