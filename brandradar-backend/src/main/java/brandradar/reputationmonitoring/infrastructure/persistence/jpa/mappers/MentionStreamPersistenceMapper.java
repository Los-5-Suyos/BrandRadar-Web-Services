package brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers;

import brandradar.reputationmonitoring.domain.model.aggregates.MentionStream;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.MentionStreamJpaEntity;

public class MentionStreamPersistenceMapper {

    private MentionStreamPersistenceMapper() {}

    public static MentionStreamJpaEntity toJpaEntity(MentionStream mentionStream) {
        return new MentionStreamJpaEntity(
                mentionStream.getId(),
                mentionStream.getBrandId(),
                mentionStream.getPeriodFrom(),
                mentionStream.getPeriodTo(),
                mentionStream.getStatus()
        );
    }

    public static MentionStream toDomain(MentionStreamJpaEntity entity) {
        return MentionStream.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getPeriodFrom(),
                entity.getPeriodTo(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}