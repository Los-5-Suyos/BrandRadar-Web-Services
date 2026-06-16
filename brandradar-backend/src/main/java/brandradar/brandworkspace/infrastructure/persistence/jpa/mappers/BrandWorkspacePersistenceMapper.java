package brandradar.brandworkspace.infrastructure.persistence.jpa.mappers;

import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceName;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceStatus;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.BrandWorkspaceJpaEntity;

public class BrandWorkspacePersistenceMapper {

    private BrandWorkspacePersistenceMapper() {}

    public static BrandWorkspaceJpaEntity toJpaEntity(BrandWorkspace workspace) {
        return new BrandWorkspaceJpaEntity(
                workspace.getId(),
                workspace.getUserId(),
                workspace.getName().value(),
                workspace.getDescription(),
                workspace.getStatus().name()
        );
    }

    public static BrandWorkspace toDomain(BrandWorkspaceJpaEntity entity) {
        return BrandWorkspace.rehydrate(
                entity.getId(),
                entity.getUserId(),
                new WorkspaceName(entity.getName()),
                entity.getDescription(),
                WorkspaceStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
