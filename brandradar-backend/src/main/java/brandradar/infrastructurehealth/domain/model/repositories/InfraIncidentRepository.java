package brandradar.infrastructurehealth.domain.model.repositories;

import brandradar.infrastructurehealth.domain.model.aggregates.InfraIncident;

import java.util.List;
import java.util.Optional;

public interface InfraIncidentRepository {
    InfraIncident save(InfraIncident incident);
    Optional<InfraIncident> findById(Long id);
    List<InfraIncident> findByServiceHealthCheckId(Long serviceHealthCheckId);
    List<InfraIncident> findByStatus(String status);
}