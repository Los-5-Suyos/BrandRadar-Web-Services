package brandradar.brandworkspace.infrastructure.persistence.jpa;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceExclusionKeyword;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceExclusionKeywordRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.mappers.WorkspaceExclusionKeywordPersistenceMapper;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataWorkspaceExclusionKeywordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WorkspaceExclusionKeywordPersistenceAdapter implements WorkspaceExclusionKeywordRepository {

    private final SpringDataWorkspaceExclusionKeywordRepository springDataRepository;

    public WorkspaceExclusionKeywordPersistenceAdapter(SpringDataWorkspaceExclusionKeywordRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public WorkspaceExclusionKeyword save(WorkspaceExclusionKeyword keyword) {
        var jpaEntity = WorkspaceExclusionKeywordPersistenceMapper.toJpaEntity(keyword);
        var saved = springDataRepository.save(jpaEntity);
        return WorkspaceExclusionKeywordPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<WorkspaceExclusionKeyword> findById(Long id) {
        return springDataRepository.findById(id)
                .map(WorkspaceExclusionKeywordPersistenceMapper::toDomain);
    }

    @Override
    public List<WorkspaceExclusionKeyword> findByWorkspaceId(Long workspaceId) {
        return springDataRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(WorkspaceExclusionKeywordPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
}