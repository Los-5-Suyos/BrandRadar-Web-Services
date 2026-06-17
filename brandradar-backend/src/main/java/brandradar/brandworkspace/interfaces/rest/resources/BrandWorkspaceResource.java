package brandradar.brandworkspace.interfaces.rest.resources;

import java.time.Instant;

public record BrandWorkspaceResource(
        Long id,
        Long userId,
        String name,
        String description,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}
