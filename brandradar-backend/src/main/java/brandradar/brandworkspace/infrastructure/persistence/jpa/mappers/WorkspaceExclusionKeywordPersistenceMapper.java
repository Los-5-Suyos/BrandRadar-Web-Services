package brandradar.brandworkspace.infrastructure.persistence.jpa.mappers;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceExclusionKeyword;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceExclusionKeywordJpaEntity;

public class WorkspaceExclusionKeywordPersistenceMapper {

    private WorkspaceExclusionKeywordPersistenceMapper() {}

    public static WorkspaceExclusionKeywordJpaEntity toJpaEntity(WorkspaceExclusionKeyword keyword) {
        return new WorkspaceExclusionKeywordJpaEntity(
                keyword.getId(),
                keyword.getWorkspaceId(),
                keyword.getKeyword()
        );
    }

    public static WorkspaceExclusionKeyword toDomain(WorkspaceExclusionKeywordJpaEntity entity) {
        return WorkspaceExclusionKeyword.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                entity.getKeyword()
        );
    }
}