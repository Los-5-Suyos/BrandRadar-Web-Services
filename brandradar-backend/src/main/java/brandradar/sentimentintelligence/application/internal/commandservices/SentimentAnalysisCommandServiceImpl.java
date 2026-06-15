package brandradar.sentimentintelligence.application.internal.commandservices;

import brandradar.sentimentintelligence.application.commands.CreateSentimentAnalysisCommand;
import brandradar.sentimentintelligence.application.commandservices.SentimentAnalysisCommandService;
import brandradar.sentimentintelligence.domain.model.aggregates.SentimentAnalysis;
import brandradar.sentimentintelligence.domain.model.repositories.SentimentAnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class SentimentAnalysisCommandServiceImpl implements SentimentAnalysisCommandService {

    private final SentimentAnalysisRepository sentimentAnalysisRepository;

    public SentimentAnalysisCommandServiceImpl(SentimentAnalysisRepository sentimentAnalysisRepository) {
        this.sentimentAnalysisRepository = sentimentAnalysisRepository;
    }

    @Override
    @Transactional
    public Optional<SentimentAnalysis> handle(CreateSentimentAnalysisCommand command) {
        var analysis = SentimentAnalysis.create(command.brandId(), command.periodFrom(), command.periodTo());
        var saved = sentimentAnalysisRepository.save(analysis);
        log.info("SentimentAnalysis created with id={}", saved.getId());
        return Optional.of(saved);
    }
}