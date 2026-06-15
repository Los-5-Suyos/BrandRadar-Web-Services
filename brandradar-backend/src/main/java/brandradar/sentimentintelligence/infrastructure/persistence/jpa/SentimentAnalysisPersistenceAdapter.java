package brandradar.sentimentintelligence.infrastructure.persistence.jpa;

import brandradar.sentimentintelligence.domain.model.aggregates.SentimentAnalysis;
import brandradar.sentimentintelligence.domain.model.repositories.SentimentAnalysisRepository;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.mappers.SentimentAnalysisPersistenceMapper;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.repositories.SpringDataSentimentAnalysisRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SentimentAnalysisPersistenceAdapter implements SentimentAnalysisRepository {

    private final SpringDataSentimentAnalysisRepository springDataRepository;

    public SentimentAnalysisPersistenceAdapter(SpringDataSentimentAnalysisRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public SentimentAnalysis save(SentimentAnalysis analysis) {
        var jpaEntity = SentimentAnalysisPersistenceMapper.toJpaEntity(analysis);
        var saved = springDataRepository.save(jpaEntity);
        return SentimentAnalysisPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<SentimentAnalysis> findById(Long id) {
        return springDataRepository.findById(id)
                .map(SentimentAnalysisPersistenceMapper::toDomain);
    }

    @Override
    public List<SentimentAnalysis> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(SentimentAnalysisPersistenceMapper::toDomain)
                .toList();
    }
}