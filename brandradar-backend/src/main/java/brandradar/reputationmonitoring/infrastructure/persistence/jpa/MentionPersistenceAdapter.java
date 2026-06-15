package brandradar.reputationmonitoring.infrastructure.persistence.jpa;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers.MentionPersistenceMapper;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories.SpringDataMentionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MentionPersistenceAdapter implements MentionRepository {

    private final SpringDataMentionRepository springDataRepository;

    public MentionPersistenceAdapter(SpringDataMentionRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Mention save(Mention mention) {
        var jpaEntity = MentionPersistenceMapper.toJpaEntity(mention);
        var saved = springDataRepository.save(jpaEntity);
        return MentionPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Mention> findById(Long id) {
        return springDataRepository.findById(id)
                .map(MentionPersistenceMapper::toDomain);
    }

    @Override
    public List<Mention> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(MentionPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Mention> findByMentionStreamId(Long mentionStreamId) {
        return springDataRepository.findByMentionStreamId(mentionStreamId)
                .stream()
                .map(MentionPersistenceMapper::toDomain)
                .toList();
    }
}