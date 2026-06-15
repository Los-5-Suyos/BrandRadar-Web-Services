package brandradar.brandworkspace.interfaces.rest.resources;

import java.time.Instant;

public record BrandWorkspaceResource(
        Long id,
        Long userId,
        String name,
        String plan,
        String status,
        Integer policyMaxBrands,
        Integer policyAlertQuota,
        Instant createdAt,
        Instant updatedAt
) {}