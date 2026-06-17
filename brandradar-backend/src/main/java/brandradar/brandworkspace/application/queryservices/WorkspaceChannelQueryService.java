package brandradar.brandworkspace.application.queryservices;

import brandradar.brandworkspace.application.queries.GetChannelsByWorkspaceIdQuery;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceChannel;

import java.util.List;

public interface WorkspaceChannelQueryService {
    List<WorkspaceChannel> handle(GetChannelsByWorkspaceIdQuery query);
}