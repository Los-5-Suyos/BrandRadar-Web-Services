package brandradar.brandworkspace.application.queryservices;

import brandradar.brandworkspace.application.queries.GetExclusionKeywordsByWorkspaceIdQuery;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceExclusionKeyword;

import java.util.List;
import java.util.Optional;

public interface ExclusionKeywordQueryService {
    List<WorkspaceExclusionKeyword> handle(GetExclusionKeywordsByWorkspaceIdQuery query);
    Optional<WorkspaceExclusionKeyword> findById(Long id);
}