package brandradar.iam.interfaces.rest.resources;

import java.time.Instant;

public record RegisteredUserResource(
        Long id,
        String email,
        String role,
        String status,
        Instant createdAt
) {}