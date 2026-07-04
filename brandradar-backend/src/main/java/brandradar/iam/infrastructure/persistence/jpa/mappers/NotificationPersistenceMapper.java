package brandradar.iam.infrastructure.persistence.jpa.mappers;

import brandradar.iam.domain.model.aggregates.Notification;
import brandradar.iam.infrastructure.persistence.jpa.entities.NotificationJpaEntity;

public class NotificationPersistenceMapper {

    private NotificationPersistenceMapper() {}

    public static NotificationJpaEntity toJpaEntity(Notification notification) {
        return new NotificationJpaEntity(
                notification.getId(),
                notification.getUserId(),
                notification.getBrandId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getIsRead()
        );
    }

    public static Notification toDomain(NotificationJpaEntity entity) {
        return Notification.rehydrate(
                entity.getId(),
                entity.getUserId(),
                entity.getBrandId(),
                entity.getType(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getIsRead(),
                entity.getCreatedAt()
        );
    }
}