package brandradar.brandworkspace.infrastructure.persistence.jpa;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceConfig;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceConfigRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.mappers.WorkspaceConfigPersistenceMapper;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataWorkspaceConfigRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WorkspaceConfigPersistenceAdapter implements WorkspaceConfigRepository {

    private final SpringDataWorkspaceConfigRepository springDataRepository;

    public WorkspaceConfigPersistenceAdapter(SpringDataWorkspaceConfigRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public WorkspaceConfig save(WorkspaceConfig config) {
        var jpaEntity = WorkspaceConfigPersistenceMapper.toJpaEntity(config);
        var saved = springDataRepository.save(jpaEntity);
        return WorkspaceConfigPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<WorkspaceConfig> findByWorkspaceId(Long workspaceId) {
        return springDataRepository.findByWorkspaceId(workspaceId)
                .map(WorkspaceConfigPersistenceMapper::toDomain);
    }
}