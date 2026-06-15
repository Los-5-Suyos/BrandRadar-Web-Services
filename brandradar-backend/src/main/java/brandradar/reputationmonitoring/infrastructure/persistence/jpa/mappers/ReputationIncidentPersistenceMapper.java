package brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationIncidentJpaEntity;

public class ReputationIncidentPersistenceMapper {

    private ReputationIncidentPersistenceMapper() {}

    public static ReputationIncidentJpaEntity toJpaEntity(ReputationIncident incident) {
        return new ReputationIncidentJpaEntity(
                incident.getId(),
                incident.getBrandId(),
                incident.getMentionStreamId(),
                incident.getSeverityLevel(),
                incident.getSeverityLabel(),
                incident.getStatus(),
                incident.getAssignedTo(),
                incident.getImpactScore(),
                incident.getResolutionSummary(),
                incident.getResolutionActions(),
                incident.getResolvedBy(),
                incident.getResolvedAt()
        );
    }

    public static ReputationIncident toDomain(ReputationIncidentJpaEntity entity) {
        return ReputationIncident.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getMentionStreamId(),
                entity.getSeverityLevel(),
                entity.getSeverityLabel(),
                entity.getStatus(),
                entity.getAssignedTo(),
                entity.getImpactScore(),
                entity.getResolutionSummary(),
                entity.getResolutionActions(),
                entity.getResolvedBy(),
                entity.getResolvedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}