package brandradar.brandworkspace.application.commands;

import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceName;

public record UpdateBrandWorkspaceCommand(
        Long id,
        Long userId,
        WorkspaceName name,
        String description
) {}
