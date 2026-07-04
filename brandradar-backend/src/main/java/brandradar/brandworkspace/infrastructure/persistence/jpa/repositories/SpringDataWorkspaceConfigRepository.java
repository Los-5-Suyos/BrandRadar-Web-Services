package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceConfigJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataWorkspaceConfigRepository extends JpaRepository<WorkspaceConfigJpaEntity, Long> {
    Optional<WorkspaceConfigJpaEntity> findByWorkspaceId(Long workspaceId);
}