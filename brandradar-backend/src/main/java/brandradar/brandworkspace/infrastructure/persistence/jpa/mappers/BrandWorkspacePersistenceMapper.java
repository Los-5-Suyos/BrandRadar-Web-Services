package brandradar.brandworkspace.infrastructure.persistence.jpa.mappers;

import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceName;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.BrandWorkspaceJpaEntity;

public class BrandWorkspacePersistenceMapper {

    private BrandWorkspacePersistenceMapper() {}

    public static BrandWorkspaceJpaEntity toJpaEntity(BrandWorkspace workspace) {
        return new BrandWorkspaceJpaEntity(
                workspace.getId(),
                workspace.getUserId(),
                workspace.getName().value(),
                workspace.getPlan(),
                workspace.getStatus(),
                workspace.getPolicyMaxBrands(),
                workspace.getPolicyAlertQuota()
        );
    }

    public static BrandWorkspace toDomain(BrandWorkspaceJpaEntity entity) {
        return BrandWorkspace.rehydrate(
                entity.getId(),
                entity.getUserId(),
                new WorkspaceName(entity.getName()),
                entity.getPlan(),
                entity.getStatus(),
                entity.getPolicyMaxBrands(),
                entity.getPolicyAlertQuota(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}