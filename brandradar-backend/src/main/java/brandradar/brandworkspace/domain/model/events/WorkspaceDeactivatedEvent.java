package brandradar.brandworkspace.domain.model.events;

import java.time.Instant;

public record WorkspaceDeactivatedEvent(Long workspaceId, Long userId, Instant occurredAt) {

    public WorkspaceDeactivatedEvent(Long workspaceId, Long userId) {
        this(workspaceId, userId, Instant.now());
    }
}
