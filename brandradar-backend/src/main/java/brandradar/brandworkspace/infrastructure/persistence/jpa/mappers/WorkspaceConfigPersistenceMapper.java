package brandradar.brandworkspace.infrastructure.persistence.jpa.mappers;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceConfig;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceConfigJpaEntity;

public class WorkspaceConfigPersistenceMapper {

    private WorkspaceConfigPersistenceMapper() {}

    public static WorkspaceConfigJpaEntity toJpaEntity(WorkspaceConfig config) {
        return new WorkspaceConfigJpaEntity(
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

    public static WorkspaceConfig toDomain(WorkspaceConfigJpaEntity entity) {
        return WorkspaceConfig.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                entity.getCompanyName(),
                entity.getIndustry(),
                entity.getWebsiteUrl(),
                entity.getYoutubeUrl(),
                entity.getFacebookUrl(),
                entity.getTwitterUrl(),
                entity.getTiktokUrl(),
                entity.getInstagramUrl(),
                entity.getRedditUrl(),
                entity.getGoogleNewsUrl(),
                entity.getLogoUrl()
        );
    }
}