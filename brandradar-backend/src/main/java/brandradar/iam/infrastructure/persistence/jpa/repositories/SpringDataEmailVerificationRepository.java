package brandradar.iam.infrastructure.persistence.jpa.repositories;

import brandradar.iam.infrastructure.persistence.jpa.entities.EmailVerificationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataEmailVerificationRepository extends JpaRepository<EmailVerificationJpaEntity, Long> {
    Optional<EmailVerificationJpaEntity> findByToken(String token);
}
