package brandradar.brandworkspace.domain.model.aggregates;

import java.util.Objects;

public class WorkspaceChannel {

    private final Long id;
    private final Long workspaceId;
    private final String channelType;

    private WorkspaceChannel(Long id, Long workspaceId, String channelType) {
        this.id = id;
        this.workspaceId = Objects.requireNonNull(workspaceId, "WorkspaceId is required");
        this.channelType = Objects.requireNonNull(channelType, "ChannelType is required");
    }

    public static WorkspaceChannel create(Long workspaceId, String channelType) {
        return new WorkspaceChannel(null, workspaceId, channelType);
    }

    public static WorkspaceChannel rehydrate(Long id, Long workspaceId, String channelType) {
        return new WorkspaceChannel(id, workspaceId, channelType);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getChannelType() { return channelType; }
}