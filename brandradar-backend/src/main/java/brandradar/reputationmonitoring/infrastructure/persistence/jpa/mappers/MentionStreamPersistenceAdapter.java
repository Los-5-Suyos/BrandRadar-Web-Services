package brandradar.reputationmonitoring.infrastructure.persistence.jpa;

import brandradar.reputationmonitoring.domain.model.aggregates.MentionStream;
import brandradar.reputationmonitoring.domain.model.repositories.MentionStreamRepository;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers.MentionStreamPersistenceMapper;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories.SpringDataMentionStreamRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MentionStreamPersistenceAdapter implements MentionStreamRepository {

    private final SpringDataMentionStreamRepository springDataRepository;

    public MentionStreamPersistenceAdapter(SpringDataMentionStreamRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public MentionStream save(MentionStream mentionStream) {
        var jpaEntity = MentionStreamPersistenceMapper.toJpaEntity(mentionStream);
        var saved = springDataRepository.save(jpaEntity);
        return MentionStreamPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<MentionStream> findById(Long id) {
        return springDataRepository.findById(id)
                .map(MentionStreamPersistenceMapper::toDomain);
    }

    @Override
    public List<MentionStream> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(MentionStreamPersistenceMapper::toDomain)
                .toList();
    }
}