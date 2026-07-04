package brandradar.brandworkspace.interfaces.rest.resources;

public record WorkspaceConfigResource(
        Long id,
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