package brandradar.brandworkspace.interfaces.rest.transform;

import brandradar.brandworkspace.application.commands.CreateBrandCommand;
import brandradar.brandworkspace.interfaces.rest.resources.CreateBrandResource;

public class CreateBrandCommandFromResourceAssembler {

    private CreateBrandCommandFromResourceAssembler() {}

    public static CreateBrandCommand toCommand(CreateBrandResource resource) {
        return new CreateBrandCommand(
                resource.workspaceId(),
                resource.name()
        );
    }
}