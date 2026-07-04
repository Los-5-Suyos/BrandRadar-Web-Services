package brandradar.shared.interfaces.rest.resources;

import java.util.List;

public record RefreshResultResource(
        Long workspaceId,
        List<BrandRefreshResult> brands
) {
    public record BrandRefreshResult(
            Long brandId,
            String brandName,
            int newMentionsCount
    ) {}
}