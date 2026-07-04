package brandradar.brandworkspace.domain.model.repositories;

import brandradar.brandworkspace.domain.model.aggregates.Subscription;

import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Optional<Subscription> findByWorkspaceId(Long workspaceId);
}