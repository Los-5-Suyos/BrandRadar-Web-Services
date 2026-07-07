package brandradar.iam.interfaces.rest.resources;

public record UpdateUserProfileResource(
        String fullName,
        String username,
        String bio,
        String language,
        String timezone,
        Boolean emailNotifications
) {}