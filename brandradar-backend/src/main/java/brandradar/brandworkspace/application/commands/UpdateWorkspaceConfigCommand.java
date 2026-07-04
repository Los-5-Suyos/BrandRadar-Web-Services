package brandradar.brandworkspace.application.commands;

public record UpdateWorkspaceConfigCommand(
        Long workspaceId,
        String companyName,
        String industry,
        String websiteUrl,
        String youtubeUrl,
        String facebookUrl,
        String twitterUrl,
        String tiktokUrl,
        String instagramUrl,
        String redditUrl,
        String googleNewsUrl,
        String logoUrl
) {}