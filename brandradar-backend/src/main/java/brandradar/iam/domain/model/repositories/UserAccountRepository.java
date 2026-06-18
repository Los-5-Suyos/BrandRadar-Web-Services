package brandradar.iam.domain.model.repositories;

import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.valueobjects.Email;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository {
    UserAccount save(UserAccount userAccount);
    Optional<UserAccount> findById(Long id);
    Optional<UserAccount> findByEmail(Email email);
    boolean existsByEmail(Email email);
    List<UserAccount> findAll();
    Optional<UserAccount> findByPasswordRecoveryToken(String token);
}