package brandradar.brandworkspace.application.internal.queryservices;

import brandradar.brandworkspace.application.queries.GetWorkspaceByIdQuery;
import brandradar.brandworkspace.application.queries.GetWorkspacesByUserIdQuery;
import brandradar.brandworkspace.application.queryservices.BrandWorkspaceQueryService;
import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandWorkspaceQueryServiceImpl implements BrandWorkspaceQueryService {

    private final BrandWorkspaceRepository brandWorkspaceRepository;

    public BrandWorkspaceQueryServiceImpl(BrandWorkspaceRepository brandWorkspaceRepository) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
    }

    @Override
    public Optional<BrandWorkspace> handle(GetWorkspaceByIdQuery query) {
        return brandWorkspaceRepository.findByIdAndUserId(query.id(), query.userId());
    }

    @Override
    public List<BrandWorkspace> handle(GetWorkspacesByUserIdQuery query) {
        return brandWorkspaceRepository.findByUserId(query.userId())
                .stream()
                .filter(workspace -> workspace.getStatus() == WorkspaceStatus.ACTIVO)
                .toList();
    }
}
