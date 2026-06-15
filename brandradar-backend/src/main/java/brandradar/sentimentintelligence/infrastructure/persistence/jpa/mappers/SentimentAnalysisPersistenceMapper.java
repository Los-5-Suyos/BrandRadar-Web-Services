package brandradar.sentimentintelligence.infrastructure.persistence.jpa.mappers;

import brandradar.sentimentintelligence.domain.model.aggregates.SentimentAnalysis;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities.SentimentAnalysisJpaEntity;

public class SentimentAnalysisPersistenceMapper {

    private SentimentAnalysisPersistenceMapper() {}

    public static SentimentAnalysisJpaEntity toJpaEntity(SentimentAnalysis analysis) {
        return new SentimentAnalysisJpaEntity(
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
                analysis.getDeltaPreviousScore(),
                analysis.getDeltaCurrentScore(),
                analysis.getDeltaChangePct()
        );
    }

    public static SentimentAnalysis toDomain(SentimentAnalysisJpaEntity entity) {
        return SentimentAnalysis.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getPeriodFrom(),
                entity.getPeriodTo(),
                entity.getScorePositive(),
                entity.getScoreNegative(),
                entity.getScoreNeutral(),
                entity.getScoreCompound(),
                entity.getTrendDirection(),
                entity.getTrendMagnitude(),
                entity.getDeltaPreviousScore(),
                entity.getDeltaCurrentScore(),
                entity.getDeltaChangePct(),
                entity.getCreatedAt()
        );
    }
}