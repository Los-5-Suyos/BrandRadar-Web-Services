package brandradar.brandworkspace.application.commands;

public record UpdateBrandWorkspaceCommand(Long workspaceId, String name, String plan) {}