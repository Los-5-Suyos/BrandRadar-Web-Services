package brandradar.sentimentintelligence.interfaces.rest.transform;

import brandradar.sentimentintelligence.application.commands.CreateSentimentAnalysisCommand;
import brandradar.sentimentintelligence.domain.model.aggregates.SentimentAnalysis;
import brandradar.sentimentintelligence.interfaces.rest.resources.CreateSentimentAnalysisResource;
import brandradar.sentimentintelligence.interfaces.rest.resources.SentimentAnalysisResource;

public class SentimentAnalysisAssembler {

    private SentimentAnalysisAssembler() {}

    public static CreateSentimentAnalysisCommand toCommand(CreateSentimentAnalysisResource resource) {
        return new CreateSentimentAnalysisCommand(
                resource.brandId(),
                resource.periodFrom(),
                resource.periodTo()
        );
    }

    public static SentimentAnalysisResource toResource(SentimentAnalysis analysis) {
        return new SentimentAnalysisResource(
                analysis.getId(),
                analysis.getBrandId(),
                analysis.getPeriodFrom(),
                analysis.getPeriodTo(),
                analysis.getScorePositive(),
                analysis.getScoreNegative(),
                analysis.getScoreNeutral(),
                analysis.getScoreCompound(),
                analysis.getTrendDirection(),
                analysis.getTrendMagnitude(),
                analysis.getDeltaChangePct(),
                analysis.getCreatedAt()
        );
    }
}