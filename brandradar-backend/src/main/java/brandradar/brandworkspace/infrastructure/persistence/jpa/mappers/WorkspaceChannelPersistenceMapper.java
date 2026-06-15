package brandradar.brandworkspace.infrastructure.persistence.jpa.mappers;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceChannel;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceChannelJpaEntity;

public class WorkspaceChannelPersistenceMapper {

    private WorkspaceChannelPersistenceMapper() {}

    public static WorkspaceChannelJpaEntity toJpaEntity(WorkspaceChannel channel) {
        return new WorkspaceChannelJpaEntity(
                channel.getId(),
                channel.getWorkspaceId(),
                channel.getChannelType()
        );
    }

    public static WorkspaceChannel toDomain(WorkspaceChannelJpaEntity entity) {
        return WorkspaceChannel.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                entity.getChannelType()
        );
    }
}