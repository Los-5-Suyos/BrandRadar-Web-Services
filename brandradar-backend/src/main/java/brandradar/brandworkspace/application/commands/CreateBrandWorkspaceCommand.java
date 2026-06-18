package brandradar.brandworkspace.application.commands;

import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceName;

public record CreateBrandWorkspaceCommand(
        Long userId,
        WorkspaceName name,
        String plan
) {}