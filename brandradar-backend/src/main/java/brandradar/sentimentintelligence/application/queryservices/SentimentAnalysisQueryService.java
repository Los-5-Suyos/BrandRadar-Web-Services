package brandradar.sentimentintelligence.application.queryservices;

import brandradar.sentimentintelligence.application.queries.GetSentimentAnalysisByBrandIdQuery;
import brandradar.sentimentintelligence.domain.model.aggregates.SentimentAnalysis;

import java.util.List;
import java.util.Optional;

public interface SentimentAnalysisQueryService {
    List<SentimentAnalysis> handle(GetSentimentAnalysisByBrandIdQuery query);
    Optional<SentimentAnalysis> findById(Long id);
}