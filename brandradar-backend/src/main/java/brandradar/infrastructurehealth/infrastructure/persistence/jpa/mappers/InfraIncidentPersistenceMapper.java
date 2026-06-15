package brandradar.infrastructurehealth.infrastructure.persistence.jpa.mappers;

import brandradar.infrastructurehealth.domain.model.aggregates.InfraIncident;
import brandradar.infrastructurehealth.infrastructure.persistence.jpa.entities.InfraIncidentJpaEntity;

public class InfraIncidentPersistenceMapper {

    private InfraIncidentPersistenceMapper() {}

    public static InfraIncidentJpaEntity toJpaEntity(InfraIncident incident) {
        return new InfraIncidentJpaEntity(
                incident.getId(),
                incident.getServiceHealthCheckId(),
                incident.getIncidentType(),
                incident.getSeverityLevel(),
                incident.getSeverityLabel(),
                incident.getStatus(),
                incident.getEstimatedReputationalImpact(),
                incident.getResolutionSummary(),
                incident.getResolutionRootCause(),
                incident.getResolutionPreventive(),
                incident.getDetectedAt(),
                incident.getResolvedAt()
        );
    }

    public static InfraIncident toDomain(InfraIncidentJpaEntity entity) {
        return InfraIncident.rehydrate(
                entity.getId(),
                entity.getServiceHealthCheckId(),
                entity.getIncidentType(),
                entity.getSeverityLevel(),
                entity.getSeverityLabel(),
                entity.getStatus(),
                entity.getEstimatedReputationalImpact(),
                entity.getResolutionSummary(),
                entity.getResolutionRootCause(),
                entity.getResolutionPreventive(),
                entity.getDetectedAt(),
                entity.getResolvedAt()
        );
    }
}