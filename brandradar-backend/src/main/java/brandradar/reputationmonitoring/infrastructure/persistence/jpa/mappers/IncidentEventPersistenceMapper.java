package brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers;

import brandradar.reputationmonitoring.domain.model.aggregates.IncidentEvent;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.IncidentEventJpaEntity;

public class IncidentEventPersistenceMapper {

    private IncidentEventPersistenceMapper() {}

    public static IncidentEventJpaEntity toJpaEntity(IncidentEvent event) {
        return new IncidentEventJpaEntity(
                event.getId(),
                event.getIncidentId(),
                event.getEventType(),
                event.getStatus(),
                event.getPerformedBy()
        );
    }

    public static IncidentEvent toDomain(IncidentEventJpaEntity entity) {
        return IncidentEvent.rehydrate(
                entity.getId(),
                entity.getIncidentId(),
                entity.getEventType(),
                entity.getStatus(),
                entity.getPerformedBy(),
                entity.getOccurredAt()
        );
    }
}