package brandradar.iam.infrastructure.persistence.jpa.repositories;

import brandradar.iam.infrastructure.persistence.jpa.entities.UserAccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUserAccountRepository extends JpaRepository<UserAccountJpaEntity, Long> {
    Optional<UserAccountJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}