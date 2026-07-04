package brandradar.iam.infrastructure.persistence.jpa.mappers;

import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import brandradar.iam.infrastructure.persistence.jpa.entities.UserAccountJpaEntity;

public class UserAccountPersistenceMapper {

    private UserAccountPersistenceMapper() {}

    public static UserAccountJpaEntity toJpaEntity(UserAccount userAccount) {
        return new UserAccountJpaEntity(
                userAccount.getId(),
                userAccount.getEmail().value(),
                userAccount.getFullName(),
                userAccount.getUsername(),
                userAccount.getPasswordHash().value(),
                userAccount.getRole(),
                userAccount.getDescription(),
                userAccount.getAvatarUrl(),
                userAccount.getBio(),
                userAccount.getLanguage(),
                userAccount.getTimezone(),
                userAccount.getEmailNotifications(),
                userAccount.getStatus(),
                userAccount.getVerificationCode()
        );
    }

    public static UserAccount toDomain(UserAccountJpaEntity entity) {
        return UserAccount.rehydrate(
                entity.getId(),
                new Email(entity.getEmail()),
                entity.getFullName(),
                entity.getUsername(),
                new PasswordHash(entity.getPasswordHash()),
                entity.getRole(),
                entity.getDescription(),
                entity.getAvatarUrl(),
                entity.getBio(),
                entity.getLanguage(),
                entity.getTimezone(),
                entity.getEmailNotifications(),
                entity.getStatus(),
                entity.getVerificationCode(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}