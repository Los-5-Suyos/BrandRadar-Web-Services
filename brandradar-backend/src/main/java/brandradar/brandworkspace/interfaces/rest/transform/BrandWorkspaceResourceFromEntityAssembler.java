package brandradar.brandworkspace.interfaces.rest.transform;

import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.interfaces.rest.resources.BrandWorkspaceResource;

public class BrandWorkspaceResourceFromEntityAssembler {

    private BrandWorkspaceResourceFromEntityAssembler() {}

    public static BrandWorkspaceResource toResourceFromEntity(BrandWorkspace workspace) {
        return new BrandWorkspaceResource(
                workspace.getId(),
                workspace.getUserId(),
                workspace.getName().value(),
                workspace.getDescription(),
                workspace.getStatus().name(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }
}
