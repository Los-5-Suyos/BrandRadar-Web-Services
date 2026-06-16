package brandradar.brandworkspace.interfaces.rest.transform;

import brandradar.brandworkspace.application.commands.CreateBrandWorkspaceCommand;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceName;
import brandradar.brandworkspace.interfaces.rest.resources.CreateBrandWorkspaceResource;

public class CreateBrandWorkspaceCommandFromResourceAssembler {

    private CreateBrandWorkspaceCommandFromResourceAssembler() {}

    public static CreateBrandWorkspaceCommand toCommand(CreateBrandWorkspaceResource resource) {
        return new CreateBrandWorkspaceCommand(
                resource.userId(),
                new WorkspaceName(resource.name()),
                resource.description()
        );
    }
}
