package brandradar.brandworkspace.interfaces.rest.resources;

import java.time.Instant;

public record SubscriptionResource(
        Long id,
        Long workspaceId,
        String plan,
        String billingPeriod,
        String status,
        String fakeCardLast4,
        String fakeCardBrand,
        Instant startedAt,
        Instant renewsAt
) {}