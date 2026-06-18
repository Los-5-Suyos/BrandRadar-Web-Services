package brandradar.sentimentintelligence.infrastructure.persistence.jpa;

import brandradar.sentimentintelligence.domain.model.aggregates.SentimentResult;
import brandradar.sentimentintelligence.domain.model.repositories.SentimentResultRepository;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.mappers.SentimentResultPersistenceMapper;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.repositories.SpringDataSentimentResultRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SentimentResultPersistenceAdapter implements SentimentResultRepository {

    private final SpringDataSentimentResultRepository springDataRepository;

    public SentimentResultPersistenceAdapter(SpringDataSentimentResultRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public SentimentResult save(SentimentResult result) {
        var jpaEntity = SentimentResultPersistenceMapper.toJpaEntity(result);
        var saved = springDataRepository.save(jpaEntity);
        return SentimentResultPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<SentimentResult> findBySentimentAnalysisId(Long sentimentAnalysisId) {
        return springDataRepository.findBySentimentAnalysisId(sentimentAnalysisId)
                .stream()
                .map(SentimentResultPersistenceMapper::toDomain)
                .toList();
    }
}