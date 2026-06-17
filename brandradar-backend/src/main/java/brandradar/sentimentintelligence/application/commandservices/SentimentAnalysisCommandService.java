package brandradar.sentimentintelligence.application.commandservices;

import brandradar.sentimentintelligence.application.commands.CreateSentimentAnalysisCommand;
import brandradar.sentimentintelligence.domain.model.aggregates.SentimentAnalysis;

import java.util.Optional;

public interface SentimentAnalysisCommandService {
    Optional<SentimentAnalysis> handle(CreateSentimentAnalysisCommand command);
}