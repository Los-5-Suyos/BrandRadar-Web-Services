package brandradar.brandworkspace.domain.model.aggregates;

import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceName;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceStatus;

import java.time.Instant;
import java.util.Objects;

public class BrandWorkspace {

    private final Long id;
    private final Long userId;
    private final WorkspaceName name;
    private final String description;
    private final WorkspaceStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    private BrandWorkspace(Long id, Long userId, WorkspaceName name, String description,
                           WorkspaceStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "UserId is required");
        this.name = Objects.requireNonNull(name, "Name is required");
        this.description = description;
        this.status = status != null ? status : WorkspaceStatus.ACTIVO;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BrandWorkspace create(Long userId, WorkspaceName name, String description) {
        return new BrandWorkspace(null, userId, name, description, WorkspaceStatus.ACTIVO, null, null);
    }

    public static BrandWorkspace rehydrate(Long id, Long userId, WorkspaceName name, String description,
                                           WorkspaceStatus status, Instant createdAt, Instant updatedAt) {
        return new BrandWorkspace(id, userId, name, description, status, createdAt, updatedAt);
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public WorkspaceName getName() { return name; }
    public String getDescription() { return description; }
    public WorkspaceStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
