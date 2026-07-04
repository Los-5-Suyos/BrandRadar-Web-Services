package brandradar.brandworkspace.application.internal.queryservices;

import brandradar.brandworkspace.application.queries.GetWorkspaceConfigByWorkspaceIdQuery;
import brandradar.brandworkspace.application.queryservices.WorkspaceConfigQueryService;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceConfig;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkspaceConfigQueryServiceImpl implements WorkspaceConfigQueryService {

    private final WorkspaceConfigRepository workspaceConfigRepository;

    public WorkspaceConfigQueryServiceImpl(WorkspaceConfigRepository workspaceConfigRepository) {
        this.workspaceConfigRepository = workspaceConfigRepository;
    }

    @Override
    public Optional<WorkspaceConfig> handle(GetWorkspaceConfigByWorkspaceIdQuery query) {
        return workspaceConfigRepository.findByWorkspaceId(query.workspaceId());
    }
}