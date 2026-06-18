package brandradar.iam.infrastructure.persistence.jpa.mappers;

import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import brandradar.iam.infrastructure.persistence.jpa.entities.UserAccountJpaEntity;

public class UserAccountPersistenceMapper {

    private UserAccountPersistenceMapper() {}

    public static UserAccountJpaEntity toJpaEntity(UserAccount userAccount) {
        UserAccountJpaEntity entity = new UserAccountJpaEntity(
                userAccount.getId(),
                userAccount.getEmail() != null ? userAccount.getEmail().value() : null,
                userAccount.getPasswordHash() != null ? userAccount.getPasswordHash().value() : null,
                userAccount.getRole(),
                userAccount.getDescription(),
                userAccount.getStatus(),
                userAccount.getFailedLoginAttempts(),
                userAccount.getPasswordRecoveryToken(),
                userAccount.getTokenExpiryDate(),
                userAccount.getSessionVersion()
        );
        entity.setCreatedAt(userAccount.getCreatedAt());
        entity.setUpdatedAt(userAccount.getUpdatedAt());
        return entity;
    }

    public static UserAccount toDomain(UserAccountJpaEntity entity) {
        return UserAccount.rehydrate(
                entity.getId(),
                entity.getEmail() != null ? new Email(entity.getEmail()) : null,
                entity.getPasswordHash() != null ? new PasswordHash(entity.getPasswordHash()) : null,
                entity.getRole(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getFailedLoginAttempts(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getPasswordRecoveryToken(),
                entity.getTokenExpiryDate(),
                entity.getSessionVersion()
        );
    }
}
