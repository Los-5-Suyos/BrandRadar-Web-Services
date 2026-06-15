package brandradar.infrastructurehealth.infrastructure.persistence.jpa.mappers;

import brandradar.infrastructurehealth.domain.model.aggregates.ServiceHealthCheck;
import brandradar.infrastructurehealth.infrastructure.persistence.jpa.entities.ServiceHealthCheckJpaEntity;

public class ServiceHealthCheckPersistenceMapper {

    private ServiceHealthCheckPersistenceMapper() {}

    public static ServiceHealthCheckJpaEntity toJpaEntity(ServiceHealthCheck healthCheck) {
        return new ServiceHealthCheckJpaEntity(
                healthCheck.getId(),
                healthCheck.getServiceName(),
                healthCheck.getEndpointUrl(),
                healthCheck.getEndpointMethod(),
                healthCheck.getEndpointTimeoutMs(),
                healthCheck.getStatus(),
                healthCheck.getUptimeTotalChecks(),
                healthCheck.getUptimeSuccessfulChecks(),
                healthCheck.getUptimeWindowDays(),
                healthCheck.getLastCheckedAt()
        );
    }

    public static ServiceHealthCheck toDomain(ServiceHealthCheckJpaEntity entity) {
        return ServiceHealthCheck.rehydrate(
                entity.getId(),
                entity.getServiceName(),
                entity.getEndpointUrl(),
                entity.getEndpointMethod(),
                entity.getEndpointTimeoutMs(),
                entity.getStatus(),
                entity.getUptimeTotalChecks(),
                entity.getUptimeSuccessfulChecks(),
                entity.getUptimeWindowDays(),
                entity.getLastCheckedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}