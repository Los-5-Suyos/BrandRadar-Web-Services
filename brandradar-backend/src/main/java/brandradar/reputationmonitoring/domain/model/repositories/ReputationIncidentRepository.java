package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;

import java.util.List;
import java.util.Optional;

public interface ReputationIncidentRepository {
    ReputationIncident save(ReputationIncident incident);
    Optional<ReputationIncident> findById(Long id);
    List<ReputationIncident> findByBrandId(Long brandId);
    List<ReputationIncident> findByStatus(String status);
    void deleteById(Long id);
}