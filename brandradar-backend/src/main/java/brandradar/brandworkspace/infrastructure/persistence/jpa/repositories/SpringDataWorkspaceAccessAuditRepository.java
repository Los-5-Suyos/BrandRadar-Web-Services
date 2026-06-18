package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceAccessAuditJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataWorkspaceAccessAuditRepository extends JpaRepository<WorkspaceAccessAuditJpaEntity, Long> {
}
