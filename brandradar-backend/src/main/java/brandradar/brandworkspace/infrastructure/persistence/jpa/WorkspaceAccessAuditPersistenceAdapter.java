package brandradar.brandworkspace.infrastructure.persistence.jpa;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceAccessAudit;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceAccessAuditRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceAccessAuditJpaEntity;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataWorkspaceAccessAuditRepository;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceAccessAuditPersistenceAdapter implements WorkspaceAccessAuditRepository {

    private final SpringDataWorkspaceAccessAuditRepository springDataRepository;

    public WorkspaceAccessAuditPersistenceAdapter(SpringDataWorkspaceAccessAuditRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public WorkspaceAccessAudit save(WorkspaceAccessAudit audit) {
        WorkspaceAccessAuditJpaEntity entity = new WorkspaceAccessAuditJpaEntity(
                audit.getWorkspaceId(),
                audit.getUserId(),
                audit.getIpAddress(),
                audit.getCreatedAt()
        );
        WorkspaceAccessAuditJpaEntity savedEntity = springDataRepository.save(entity);
        return WorkspaceAccessAudit.rehydrate(
                savedEntity.getId(),
                savedEntity.getWorkspaceId(),
                savedEntity.getUserId(),
                savedEntity.getIpAddress(),
                savedEntity.getCreatedAt()
        );
    }
}
