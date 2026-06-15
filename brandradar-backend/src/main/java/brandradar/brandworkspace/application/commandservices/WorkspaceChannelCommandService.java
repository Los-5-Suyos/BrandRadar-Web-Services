package brandradar.brandworkspace.application.commandservices;

import brandradar.brandworkspace.application.commands.AddWorkspaceChannelCommand;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceChannel;

import java.util.Optional;

public interface WorkspaceChannelCommandService {
    Optional<WorkspaceChannel> handle(AddWorkspaceChannelCommand command);
    void deleteByWorkspaceIdAndChannelType(Long workspaceId, String channelType);
}