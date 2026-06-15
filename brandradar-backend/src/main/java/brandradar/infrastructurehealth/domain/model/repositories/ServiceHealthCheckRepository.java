package brandradar.infrastructurehealth.domain.model.repositories;

import brandradar.infrastructurehealth.domain.model.aggregates.ServiceHealthCheck;

import java.util.List;
import java.util.Optional;

public interface ServiceHealthCheckRepository {
    ServiceHealthCheck save(ServiceHealthCheck healthCheck);
    Optional<ServiceHealthCheck> findById(Long id);
    List<ServiceHealthCheck> findAll();
    List<ServiceHealthCheck> findByStatus(String status);
}