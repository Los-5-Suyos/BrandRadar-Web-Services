package brandradar.sentimentintelligence.domain.model.repositories;

import brandradar.sentimentintelligence.domain.model.aggregates.SentimentResult;

import java.util.List;

public interface SentimentResultRepository {
    SentimentResult save(SentimentResult result);
    List<SentimentResult> findBySentimentAnalysisId(Long sentimentAnalysisId);
}