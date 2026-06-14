package brandradar.iam.interfaces.rest.resources;

import java.time.Instant;

public record UserAccountResource(
        Long id,
        String email,
        String role,
        String description,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}