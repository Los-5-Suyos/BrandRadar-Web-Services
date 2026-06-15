package brandradar.brandworkspace.application.queryservices;

import brandradar.brandworkspace.application.queries.GetWorkspaceByIdQuery;
import brandradar.brandworkspace.application.queries.GetWorkspacesByUserIdQuery;
import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;

import java.util.List;
import java.util.Optional;

public interface BrandWorkspaceQueryService {
    Optional<BrandWorkspace> handle(GetWorkspaceByIdQuery query);
    List<BrandWorkspace> handle(GetWorkspacesByUserIdQuery query);
}