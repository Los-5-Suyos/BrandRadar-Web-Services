package brandradar.brandworkspace.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record BrandResource(
        Long id,
        Long workspaceId,
        String name,
        BigDecimal reputationScore,
        Instant reputationCalculatedAt,
        Instant createdAt,
        Instant updatedAt
) {}