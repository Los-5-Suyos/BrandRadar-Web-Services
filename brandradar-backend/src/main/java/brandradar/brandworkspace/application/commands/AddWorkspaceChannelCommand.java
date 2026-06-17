package brandradar.brandworkspace.application.commands;

public record AddWorkspaceChannelCommand(
        Long workspaceId,
        String channelType
) {}