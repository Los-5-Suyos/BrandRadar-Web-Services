package brandradar.brandworkspace.interfaces.rest.transform;

import brandradar.brandworkspace.application.commands.UpdateWorkspaceConfigCommand;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceConfig;
import brandradar.brandworkspace.interfaces.rest.resources.UpdateWorkspaceConfigResource;
import brandradar.brandworkspace.interfaces.rest.resources.WorkspaceConfigResource;

public class WorkspaceConfigAssembler {

    private WorkspaceConfigAssembler() {}

    public static UpdateWorkspaceConfigCommand toCommand(Long workspaceId, UpdateWorkspaceConfigResource resource) {
        return new UpdateWorkspaceConfigCommand(
                workspaceId,
                resource.companyName(),
                resource.industry(),
                resource.websiteUrl(),
                resource.youtubeUrl(),
                resource.facebookUrl(),
                resource.twitterUrl(),
                resource.tiktokUrl(),
                resource.instagramUrl(),
                resource.redditUrl(),
                resource.googleNewsUrl(),
                resource.logoUrl()
        );
    }

    public static WorkspaceConfigResource toResource(WorkspaceConfig config) {
        return new WorkspaceConfigResource(
                config.getId(),
                config.getWorkspaceId(),
                config.getCompanyName(),
                config.getIndustry(),
                config.getWebsiteUrl(),
                config.getYoutubeUrl(),
                config.getFacebookUrl(),
                config.getTwitterUrl(),
                config.getTiktokUrl(),
                config.getInstagramUrl(),
                config.getRedditUrl(),
                config.getGoogleNewsUrl(),
                config.getLogoUrl()
        );
    }
}