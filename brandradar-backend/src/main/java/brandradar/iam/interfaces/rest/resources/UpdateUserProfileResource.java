package brandradar.iam.interfaces.rest.resources;

public record UpdateUserProfileResource(
        String fullName,
        String bio,
        String language,
        String timezone,
        Boolean emailNotifications
) {}