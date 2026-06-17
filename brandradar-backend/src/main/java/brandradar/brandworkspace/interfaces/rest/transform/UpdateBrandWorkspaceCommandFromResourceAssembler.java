package brandradar.brandworkspace.interfaces.rest.transform;

import brandradar.brandworkspace.application.commands.UpdateBrandWorkspaceCommand;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceName;
import brandradar.brandworkspace.interfaces.rest.resources.UpdateBrandWorkspaceResource;

public class UpdateBrandWorkspaceCommandFromResourceAssembler {

    private UpdateBrandWorkspaceCommandFromResourceAssembler() {}

    public static UpdateBrandWorkspaceCommand toCommand(Long id, Long userId, UpdateBrandWorkspaceResource resource) {
        return new UpdateBrandWorkspaceCommand(
                id,
                userId,
                new WorkspaceName(resource.name()),
                resource.description()
        );
    }
}
