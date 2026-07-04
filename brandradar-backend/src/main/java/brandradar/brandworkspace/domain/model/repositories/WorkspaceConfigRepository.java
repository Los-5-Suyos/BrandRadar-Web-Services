package brandradar.brandworkspace.domain.model.repositories;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceConfig;

import java.util.Optional;

public interface WorkspaceConfigRepository {
    WorkspaceConfig save(WorkspaceConfig config);
    Optional<WorkspaceConfig> findByWorkspaceId(Long workspaceId);
}