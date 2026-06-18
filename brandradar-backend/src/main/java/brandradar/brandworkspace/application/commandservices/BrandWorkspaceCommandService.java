package brandradar.brandworkspace.application.commandservices;

import brandradar.brandworkspace.application.commands.CreateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commands.DeactivateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commands.UpdateBrandWorkspaceCommand;
import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;

import java.util.Optional;

public interface BrandWorkspaceCommandService {
    Optional<BrandWorkspace> handle(CreateBrandWorkspaceCommand command);
    BrandWorkspace handle(UpdateBrandWorkspaceCommand command);
    void handle(DeactivateBrandWorkspaceCommand command);
}
