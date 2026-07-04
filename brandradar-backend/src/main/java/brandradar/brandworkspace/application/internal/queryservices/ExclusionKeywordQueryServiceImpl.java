package brandradar.brandworkspace.application.internal.queryservices;

import brandradar.brandworkspace.application.queries.GetExclusionKeywordsByWorkspaceIdQuery;
import brandradar.brandworkspace.application.queryservices.ExclusionKeywordQueryService;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceExclusionKeyword;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceExclusionKeywordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExclusionKeywordQueryServiceImpl implements ExclusionKeywordQueryService {

    private final WorkspaceExclusionKeywordRepository exclusionKeywordRepository;

    public ExclusionKeywordQueryServiceImpl(WorkspaceExclusionKeywordRepository exclusionKeywordRepository) {
        this.exclusionKeywordRepository = exclusionKeywordRepository;
    }

    @Override
    public List<WorkspaceExclusionKeyword> handle(GetExclusionKeywordsByWorkspaceIdQuery query) {
        return exclusionKeywordRepository.findByWorkspaceId(query.workspaceId());
    }

    @Override
    public Optional<WorkspaceExclusionKeyword> findById(Long id) {
        return exclusionKeywordRepository.findById(id);
    }
}