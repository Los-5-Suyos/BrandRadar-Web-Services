package brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentLabel;
import brandradar.reputationmonitoring.domain.model.valueobjects.SourceType;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.MentionJpaEntity;

public class MentionPersistenceMapper {

    private MentionPersistenceMapper() {}

    public static MentionJpaEntity toJpaEntity(Mention mention) {
        return new MentionJpaEntity(
                mention.getId(),
                mention.getWorkspaceId(),
                mention.getSource().name(),
                mention.getAuthorName(),
                mention.getAuthorFollowers(),
                mention.getContent(),
                mention.getUrl(),
                mention.getSentimentScore(),
                mention.getSentimentLabel().name(),
                mention.getPublishedAt(),
                mention.isActive()
        );
    }

    public static Mention toDomain(MentionJpaEntity entity) {
        return Mention.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                SourceType.valueOf(entity.getSource()),
                entity.getAuthorName(),
                entity.getAuthorFollowers(),
                entity.getContent(),
                entity.getUrl(),
                entity.getSentimentScore(),
                SentimentLabel.valueOf(entity.getSentimentLabel()),
                entity.getPublishedAt(),
                entity.getCreatedAt(),
                entity.isActive()
        );
    }
}
