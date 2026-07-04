package brandradar.iam.application.commands;

public record UpdateUserProfileCommand(
        Long userId,
        String fullName,
        String bio,
        String language,
        String timezone,
        Boolean emailNotifications,
        String avatarUrl
) {}