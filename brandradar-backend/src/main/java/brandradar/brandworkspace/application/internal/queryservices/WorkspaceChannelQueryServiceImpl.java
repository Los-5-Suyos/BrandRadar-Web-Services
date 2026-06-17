package brandradar.brandworkspace.application.internal.queryservices;

import brandradar.brandworkspace.application.queries.GetChannelsByWorkspaceIdQuery;
import brandradar.brandworkspace.application.queryservices.WorkspaceChannelQueryService;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceChannel;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkspaceChannelQueryServiceImpl implements WorkspaceChannelQueryService {

    private final WorkspaceChannelRepository workspaceChannelRepository;

    public WorkspaceChannelQueryServiceImpl(WorkspaceChannelRepository workspaceChannelRepository) {
        this.workspaceChannelRepository = workspaceChannelRepository;
    }

    @Override
    public List<WorkspaceChannel> handle(GetChannelsByWorkspaceIdQuery query) {
        return workspaceChannelRepository.findByWorkspaceId(query.workspaceId());
    }
}