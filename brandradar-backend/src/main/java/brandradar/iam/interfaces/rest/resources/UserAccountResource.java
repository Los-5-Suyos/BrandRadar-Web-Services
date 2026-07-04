package brandradar.iam.interfaces.rest.resources;

import java.time.Instant;

public record UserAccountResource(
        Long id,
        String email,
        String fullName,
        String role,
        String description,
        String avatarUrl,
        String bio,
        String language,
        String timezone,
        Boolean emailNotifications,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}