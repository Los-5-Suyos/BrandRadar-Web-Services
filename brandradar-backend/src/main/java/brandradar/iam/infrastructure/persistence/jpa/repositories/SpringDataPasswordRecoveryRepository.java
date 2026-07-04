package brandradar.iam.infrastructure.persistence.jpa.repositories;

import brandradar.iam.infrastructure.persistence.jpa.entities.PasswordRecoveryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataPasswordRecoveryRepository extends JpaRepository<PasswordRecoveryJpaEntity, Long> {
    Optional<PasswordRecoveryJpaEntity> findByToken(String token);
}