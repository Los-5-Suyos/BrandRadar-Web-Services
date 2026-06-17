package brandradar.sentimentintelligence.infrastructure.persistence.jpa.mappers;

import brandradar.sentimentintelligence.domain.model.aggregates.SentimentResult;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities.SentimentResultJpaEntity;

public class SentimentResultPersistenceMapper {

    private SentimentResultPersistenceMapper() {}

    public static SentimentResultJpaEntity toJpaEntity(SentimentResult result) {
        return new SentimentResultJpaEntity(
                result.getId(),
                result.getSentimentAnalysisId(),
                result.getMentionId(),
                result.getScorePositive(),
                result.getScoreNegative(),
                result.getScoreNeutral(),
                result.getScoreCompound(),
                result.getDominantEmotion(),
                result.getLanguage()
        );
    }

    public static SentimentResult toDomain(SentimentResultJpaEntity entity) {
        return SentimentResult.rehydrate(
                entity.getId(),
                entity.getSentimentAnalysisId(),
                entity.getMentionId(),
                entity.getScorePositive(),
                entity.getScoreNegative(),
                entity.getScoreNeutral(),
                entity.getScoreCompound(),
                entity.getDominantEmotion(),
                entity.getLanguage(),
                entity.getAnalyzedAt()
        );
    }
}