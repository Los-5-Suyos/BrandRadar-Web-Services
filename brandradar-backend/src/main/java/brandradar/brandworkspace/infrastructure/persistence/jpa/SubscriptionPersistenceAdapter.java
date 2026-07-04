package brandradar.brandworkspace.infrastructure.persistence.jpa;

import brandradar.brandworkspace.domain.model.aggregates.Subscription;
import brandradar.brandworkspace.domain.model.repositories.SubscriptionRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.mappers.SubscriptionPersistenceMapper;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataSubscriptionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SubscriptionPersistenceAdapter implements SubscriptionRepository {

    private final SpringDataSubscriptionRepository springDataRepository;

    public SubscriptionPersistenceAdapter(SpringDataSubscriptionRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        var jpaEntity = SubscriptionPersistenceMapper.toJpaEntity(subscription);
        var saved = springDataRepository.save(jpaEntity);
        return SubscriptionPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Subscription> findByWorkspaceId(Long workspaceId) {
        return springDataRepository.findByWorkspaceId(workspaceId)
                .map(SubscriptionPersistenceMapper::toDomain);
    }
}