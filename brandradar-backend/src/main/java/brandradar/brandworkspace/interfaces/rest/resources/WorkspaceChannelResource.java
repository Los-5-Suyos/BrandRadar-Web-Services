package brandradar.brandworkspace.interfaces.rest.resources;

public record WorkspaceChannelResource(
        Long id,
        Long workspaceId,
        String channelType
) {}