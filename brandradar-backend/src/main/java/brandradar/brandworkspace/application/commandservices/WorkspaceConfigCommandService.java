package brandradar.brandworkspace.application.commandservices;

import brandradar.brandworkspace.application.commands.UpdateWorkspaceConfigCommand;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceConfig;

public interface WorkspaceConfigCommandService {
    WorkspaceConfig handle(UpdateWorkspaceConfigCommand command);
}