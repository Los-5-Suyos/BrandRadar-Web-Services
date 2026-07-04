package brandradar.brandworkspace.domain.model.repositories;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceExclusionKeyword;

import java.util.List;
import java.util.Optional;

public interface WorkspaceExclusionKeywordRepository {
    WorkspaceExclusionKeyword save(WorkspaceExclusionKeyword keyword);
    Optional<WorkspaceExclusionKeyword> findById(Long id);
    List<WorkspaceExclusionKeyword> findByWorkspaceId(Long workspaceId);
    void deleteById(Long id);
}