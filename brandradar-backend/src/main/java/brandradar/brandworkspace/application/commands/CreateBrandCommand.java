package brandradar.brandworkspace.application.commands;

public record CreateBrandCommand(
        Long workspaceId,
        String name
) {}