package brandradar.iam.infrastructure.persistence.jpa.mappers;

import brandradar.iam.domain.model.aggregates.PasswordRecovery;
import brandradar.iam.infrastructure.persistence.jpa.entities.PasswordRecoveryJpaEntity;

public class PasswordRecoveryPersistenceMapper {

    private PasswordRecoveryPersistenceMapper() {}

    public static PasswordRecoveryJpaEntity toJpaEntity(PasswordRecovery recovery) {
        return new PasswordRecoveryJpaEntity(
                recovery.getId(),
                recovery.getUserId(),
                recovery.getToken(),
                recovery.getStatus(),
                recovery.getExpiresAt(),
                recovery.getUsedAt()
        );
    }

    public static PasswordRecovery toDomain(PasswordRecoveryJpaEntity entity) {
        return PasswordRecovery.rehydrate(
                entity.getId(),
                entity.getUserId(),
                entity.getToken(),
                entity.getStatus(),
                entity.getExpiresAt(),
                entity.getUsedAt()
        );
    }
}