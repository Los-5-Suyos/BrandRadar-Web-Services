package brandradar.iam.interfaces.rest.resources;

import java.time.Instant;

public record NotificationResource(
        Long id,
        Long brandId,
        String type,
        String title,
        String message,
        Boolean isRead,
        Instant createdAt
) {}