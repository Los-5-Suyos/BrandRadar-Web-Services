package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceExclusionKeywordJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataWorkspaceExclusionKeywordRepository extends JpaRepository<WorkspaceExclusionKeywordJpaEntity, Long> {
    List<WorkspaceExclusionKeywordJpaEntity> findByWorkspaceId(Long workspaceId);
}