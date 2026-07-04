package brandradar.brandworkspace.infrastructure.persistence.jpa.mappers;

import brandradar.brandworkspace.domain.model.aggregates.Subscription;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.SubscriptionJpaEntity;

public class SubscriptionPersistenceMapper {

    private SubscriptionPersistenceMapper() {}

    public static SubscriptionJpaEntity toJpaEntity(Subscription subscription) {
        return new SubscriptionJpaEntity(
                subscription.getId(),
                subscription.getWorkspaceId(),
                subscription.getPlan(),
                subscription.getBillingPeriod(),
                subscription.getStatus(),
                subscription.getFakeCardLast4(),
                subscription.getFakeCardBrand(),
                subscription.getStartedAt(),
                subscription.getRenewsAt()
        );
    }

    public static Subscription toDomain(SubscriptionJpaEntity entity) {
        return Subscription.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                entity.getPlan(),
                entity.getBillingPeriod(),
                entity.getStatus(),
                entity.getFakeCardLast4(),
                entity.getFakeCardBrand(),
                entity.getStartedAt(),
                entity.getRenewsAt()
        );
    }
}