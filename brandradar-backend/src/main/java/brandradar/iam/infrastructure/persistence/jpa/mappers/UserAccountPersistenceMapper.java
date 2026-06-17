package brandradar.iam.infrastructure.persistence.jpa.mappers;

import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import brandradar.iam.infrastructure.persistence.jpa.entities.UserAccountJpaEntity;

public class UserAccountPersistenceMapper {

    private UserAccountPersistenceMapper() {}

    public static UserAccountJpaEntity toJpaEntity(UserAccount userAccount) {
        // Construimos la entidad JPA pasando los valores exactos con sus paréntesis de método
        UserAccountJpaEntity entity = new UserAccountJpaEntity(
                userAccount.getId(),
                userAccount.getEmail() != null ? userAccount.getEmail().value() : null,
                userAccount.getPasswordHash() != null ? userAccount.getPasswordHash().value() : null,
                userAccount.getRole(),
                userAccount.getDescription(),
                userAccount.getStatus(),
                userAccount.getPasswordRecoveryToken(),
                userAccount.getTokenExpiryDate(),
                userAccount.getSessionVersion()
        );

        // Seteamos los campos nuevos del Sprint 3 usando los setters que añadimos con Lombok
        entity.setPasswordRecoveryToken(userAccount.getPasswordRecoveryToken());
        entity.setTokenExpiryDate(userAccount.getTokenExpiryDate());
        entity.setSessionVersion(userAccount.getSessionVersion());

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
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getPasswordRecoveryToken(),
                entity.getTokenExpiryDate(),
                entity.getSessionVersion()
        );
    }
}