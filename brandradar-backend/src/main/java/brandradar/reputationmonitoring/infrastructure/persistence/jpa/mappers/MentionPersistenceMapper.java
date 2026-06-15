package brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.MentionJpaEntity;

public class MentionPersistenceMapper {

    private MentionPersistenceMapper() {}

    public static MentionJpaEntity toJpaEntity(Mention mention) {
        return new MentionJpaEntity(
                mention.getId(),
                mention.getMentionStreamId(),
                mention.getBrandId(),
                mention.getContent(),
                mention.getSourcePlatform(),
                mention.getSourceUrl(),
                mention.getSourceReliability(),
                mention.getAuthor(),
                mention.getPublishedAt(),
                mention.getCategory(),
                mention.getSentimentPositive(),
                mention.getSentimentNegative(),
                mention.getSentimentNeutral(),
                mention.getSentimentCompound(),
                mention.getSentimentConfidence()
        );
    }

    public static Mention toDomain(MentionJpaEntity entity) {
        return Mention.rehydrate(
                entity.getId(),
                entity.getMentionStreamId(),
                entity.getBrandId(),
                entity.getContent(),
                entity.getSourcePlatform(),
                entity.getSourceUrl(),
                entity.getSourceReliability(),
                entity.getAuthor(),
                entity.getPublishedAt(),
                entity.getCategory(),
                entity.getSentimentPositive(),
                entity.getSentimentNegative(),
                entity.getSentimentNeutral(),
                entity.getSentimentCompound(),
                entity.getSentimentConfidence(),
                entity.getCreatedAt()
        );
    }
}