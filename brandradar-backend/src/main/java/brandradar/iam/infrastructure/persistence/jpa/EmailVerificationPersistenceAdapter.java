package brandradar.iam.infrastructure.persistence.jpa;

import brandradar.iam.domain.model.aggregates.EmailVerification;
import brandradar.iam.domain.model.repositories.EmailVerificationRepository;
import brandradar.iam.infrastructure.persistence.jpa.entities.EmailVerificationJpaEntity;
import brandradar.iam.infrastructure.persistence.jpa.repositories.SpringDataEmailVerificationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmailVerificationPersistenceAdapter implements EmailVerificationRepository {

    private final SpringDataEmailVerificationRepository springDataRepository;

    public EmailVerificationPersistenceAdapter(SpringDataEmailVerificationRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public EmailVerification save(EmailVerification verification) {
        EmailVerificationJpaEntity entity = new EmailVerificationJpaEntity(
                verification.getUserId(),
                verification.getToken(),
                verification.getStatus(),
                verification.getExpiresAt(),
                verification.getUsedAt(),
                verification.getCreatedAt()
        );
        if (verification.getId() != null) {
            entity.setId(verification.getId());
        }
        EmailVerificationJpaEntity saved = springDataRepository.save(entity);
        return EmailVerification.rehydrate(
                saved.getId(),
                saved.getUserId(),
                saved.getToken(),
                saved.getStatus(),
                saved.getExpiresAt(),
                saved.getUsedAt(),
                saved.getCreatedAt()
        );
    }

    @Override
    public Optional<EmailVerification> findByToken(String token) {
        return springDataRepository.findByToken(token)
                .map(entity -> EmailVerification.rehydrate(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getToken(),
                        entity.getStatus(),
                        entity.getExpiresAt(),
                        entity.getUsedAt(),
                        entity.getCreatedAt()
                ));
    }
}
