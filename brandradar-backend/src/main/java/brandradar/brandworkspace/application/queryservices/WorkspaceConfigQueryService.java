package brandradar.brandworkspace.application.queryservices;

import brandradar.brandworkspace.application.queries.GetWorkspaceConfigByWorkspaceIdQuery;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceConfig;

import java.util.Optional;

public interface WorkspaceConfigQueryService {
    Optional<WorkspaceConfig> handle(GetWorkspaceConfigByWorkspaceIdQuery query);
}