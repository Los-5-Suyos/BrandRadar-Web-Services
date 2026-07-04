package brandradar.iam.domain.model.repositories;

import brandradar.iam.domain.model.aggregates.PasswordRecovery;

import java.util.Optional;

public interface PasswordRecoveryRepository {
    PasswordRecovery save(PasswordRecovery recovery);
    Optional<PasswordRecovery> findByToken(String token);
}