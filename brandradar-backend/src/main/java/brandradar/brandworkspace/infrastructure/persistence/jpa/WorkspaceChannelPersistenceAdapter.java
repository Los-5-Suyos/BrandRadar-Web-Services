package brandradar.brandworkspace.infrastructure.persistence.jpa;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceChannel;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceChannelRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.mappers.WorkspaceChannelPersistenceMapper;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataWorkspaceChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WorkspaceChannelPersistenceAdapter implements WorkspaceChannelRepository {

    private final SpringDataWorkspaceChannelRepository springDataRepository;

    public WorkspaceChannelPersistenceAdapter(SpringDataWorkspaceChannelRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public WorkspaceChannel save(WorkspaceChannel channel) {
        var jpaEntity = WorkspaceChannelPersistenceMapper.toJpaEntity(channel);
        var saved = springDataRepository.save(jpaEntity);
        return WorkspaceChannelPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<WorkspaceChannel> findById(Long id) {
        return springDataRepository.findById(id)
                .map(WorkspaceChannelPersistenceMapper::toDomain);
    }

    @Override
    public List<WorkspaceChannel> findByWorkspaceId(Long workspaceId) {
        return springDataRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(WorkspaceChannelPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<WorkspaceChannel> findByWorkspaceIdAndChannelType(Long workspaceId, String channelType) {
        return springDataRepository.findByWorkspaceIdAndChannelType(workspaceId, channelType)
                .map(WorkspaceChannelPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsByWorkspaceIdAndChannelType(Long workspaceId, String channelType) {
        return springDataRepository.existsByWorkspaceIdAndChannelType(workspaceId, channelType);
    }
}