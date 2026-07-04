package brandradar.sentimentintelligence.infrastructure.persistence.jpa;

import brandradar.sentimentintelligence.domain.model.aggregates.ChannelInsight;
import brandradar.sentimentintelligence.domain.model.repositories.ChannelInsightRepository;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.mappers.ChannelInsightPersistenceMapper;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.repositories.SpringDataChannelInsightRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ChannelInsightPersistenceAdapter implements ChannelInsightRepository {

    private final SpringDataChannelInsightRepository springDataRepository;

    public ChannelInsightPersistenceAdapter(SpringDataChannelInsightRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ChannelInsight save(ChannelInsight insight) {
        var jpaEntity = ChannelInsightPersistenceMapper.toJpaEntity(insight);
        var saved = springDataRepository.save(jpaEntity);
        return ChannelInsightPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ChannelInsight> findByBrandIdAndChannelType(Long brandId, String channelType) {
        return springDataRepository.findByBrandIdAndChannelType(brandId, channelType)
                .map(ChannelInsightPersistenceMapper::toDomain);
    }

    @Override
    public List<ChannelInsight> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(ChannelInsightPersistenceMapper::toDomain)
                .toList();
    }
}