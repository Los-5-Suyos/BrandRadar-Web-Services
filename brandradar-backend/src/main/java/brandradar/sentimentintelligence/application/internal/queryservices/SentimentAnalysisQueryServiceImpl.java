package brandradar.sentimentintelligence.application.internal.queryservices;

import brandradar.sentimentintelligence.application.queries.GetSentimentAnalysisByBrandIdQuery;
import brandradar.sentimentintelligence.application.queryservices.SentimentAnalysisQueryService;
import brandradar.sentimentintelligence.domain.model.aggregates.SentimentAnalysis;
import brandradar.sentimentintelligence.domain.model.repositories.SentimentAnalysisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SentimentAnalysisQueryServiceImpl implements SentimentAnalysisQueryService {

    private final SentimentAnalysisRepository sentimentAnalysisRepository;

    public SentimentAnalysisQueryServiceImpl(SentimentAnalysisRepository sentimentAnalysisRepository) {
        this.sentimentAnalysisRepository = sentimentAnalysisRepository;
    }

    @Override
    public List<SentimentAnalysis> handle(GetSentimentAnalysisByBrandIdQuery query) {
        return sentimentAnalysisRepository.findByBrandId(query.brandId());
    }

    @Override
    public Optional<SentimentAnalysis> findById(Long id) {
        return sentimentAnalysisRepository.findById(id);
    }
}