package brandradar.iam.domain.model.repositories;

import brandradar.iam.domain.model.aggregates.EmailVerification;

import java.util.Optional;

public interface EmailVerificationRepository {
    EmailVerification save(EmailVerification verification);
    Optional<EmailVerification> findByToken(String token);
}
