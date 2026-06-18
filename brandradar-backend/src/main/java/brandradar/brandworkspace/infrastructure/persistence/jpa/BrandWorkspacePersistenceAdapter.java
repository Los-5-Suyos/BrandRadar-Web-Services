package brandradar.brandworkspace.infrastructure.persistence.jpa;

import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceStatus;
import brandradar.brandworkspace.infrastructure.persistence.jpa.mappers.BrandWorkspacePersistenceMapper;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataBrandWorkspaceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BrandWorkspacePersistenceAdapter implements BrandWorkspaceRepository {

    private final SpringDataBrandWorkspaceRepository springDataRepository;

    public BrandWorkspacePersistenceAdapter(SpringDataBrandWorkspaceRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public BrandWorkspace save(BrandWorkspace workspace) {
        var jpaEntity = BrandWorkspacePersistenceMapper.toJpaEntity(workspace);
        var saved = springDataRepository.save(jpaEntity);
        return BrandWorkspacePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<BrandWorkspace> findById(Long id) {
        return springDataRepository.findById(id)
                .map(BrandWorkspacePersistenceMapper::toDomain);
    }

    @Override
    public List<BrandWorkspace> findByUserId(Long userId) {
        return springDataRepository.findByUserId(userId)
                .stream()
                .map(BrandWorkspacePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<BrandWorkspace> findByIdAndUserId(Long id, Long userId) {
        return springDataRepository.findByIdAndUserId(id, userId)
                .map(BrandWorkspacePersistenceMapper::toDomain);
    }

    @Override
    public List<BrandWorkspace> findByStatus(WorkspaceStatus status) {
        return springDataRepository.findByStatus(status.name())
                .stream()
                .map(BrandWorkspacePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<BrandWorkspace> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(BrandWorkspacePersistenceMapper::toDomain)
                .toList();
    }
}
