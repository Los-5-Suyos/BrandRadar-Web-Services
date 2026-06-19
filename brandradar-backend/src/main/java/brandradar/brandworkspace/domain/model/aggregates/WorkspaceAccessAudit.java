package brandradar.brandworkspace.domain.model.aggregates;

import java.time.Instant;

public class WorkspaceAccessAudit {
    private final Long id;
    private final Long workspaceId;
    private final Long userId;
    private final String ipAddress;
    private final Instant createdAt;

    private WorkspaceAccessAudit(Long id, Long workspaceId, Long userId, String ipAddress, Instant createdAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public static WorkspaceAccessAudit create(Long workspaceId, Long userId, String ipAddress) {
        return new WorkspaceAccessAudit(null, workspaceId, userId, ipAddress, Instant.now());
    }

    public static WorkspaceAccessAudit rehydrate(Long id, Long workspaceId, Long userId, String ipAddress, Instant createdAt) {
        return new WorkspaceAccessAudit(id, workspaceId, userId, ipAddress, createdAt);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public Long getUserId() { return userId; }
    public String getIpAddress() { return ipAddress; }
    public Instant getCreatedAt() { return createdAt; }
}
