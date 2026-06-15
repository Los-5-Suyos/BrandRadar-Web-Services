package brandradar.brandworkspace.interfaces.rest.transform;

import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.interfaces.rest.resources.BrandResource;

public class BrandResourceFromEntityAssembler {

    private BrandResourceFromEntityAssembler() {}

    public static BrandResource toResourceFromEntity(Brand brand) {
        return new BrandResource(
                brand.getId(),
                brand.getWorkspaceId(),
                brand.getName(),
                brand.getReputationScore(),
                brand.getReputationCalculatedAt(),
                brand.getCreatedAt(),
                brand.getUpdatedAt()
        );
    }
}