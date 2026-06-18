package brandradar.brandworkspace.domain.model.repositories;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceChannel;

import java.util.List;
import java.util.Optional;

public interface WorkspaceChannelRepository {
    WorkspaceChannel save(WorkspaceChannel channel);
    Optional<WorkspaceChannel> findById(Long id);
    List<WorkspaceChannel> findByWorkspaceId(Long workspaceId);
    Optional<WorkspaceChannel> findByWorkspaceIdAndChannelType(Long workspaceId, String channelType);
    void deleteById(Long id);
    boolean existsByWorkspaceIdAndChannelType(Long workspaceId, String channelType);
}