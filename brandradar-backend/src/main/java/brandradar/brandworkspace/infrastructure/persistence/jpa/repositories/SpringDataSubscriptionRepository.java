package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.SubscriptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataSubscriptionRepository extends JpaRepository<SubscriptionJpaEntity, Long> {
    Optional<SubscriptionJpaEntity> findByWorkspaceId(Long workspaceId);
}