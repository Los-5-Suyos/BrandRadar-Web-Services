package brandradar.sentimentintelligence.domain.model.repositories;

import brandradar.sentimentintelligence.domain.model.aggregates.SentimentAnalysis;

import java.util.List;
import java.util.Optional;

public interface SentimentAnalysisRepository {
    SentimentAnalysis save(SentimentAnalysis analysis);
    Optional<SentimentAnalysis> findById(Long id);
    List<SentimentAnalysis> findByBrandId(Long brandId);
    void deleteById(Long id);
}