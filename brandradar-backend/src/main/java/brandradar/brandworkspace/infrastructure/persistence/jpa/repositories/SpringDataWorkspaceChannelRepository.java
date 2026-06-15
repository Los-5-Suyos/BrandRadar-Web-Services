package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceChannelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataWorkspaceChannelRepository extends JpaRepository<WorkspaceChannelJpaEntity, Long> {
    List<WorkspaceChannelJpaEntity> findByWorkspaceId(Long workspaceId);
    Optional<WorkspaceChannelJpaEntity> findByWorkspaceIdAndChannelType(Long workspaceId, String channelType);
    boolean existsByWorkspaceIdAndChannelType(Long workspaceId, String channelType);
}