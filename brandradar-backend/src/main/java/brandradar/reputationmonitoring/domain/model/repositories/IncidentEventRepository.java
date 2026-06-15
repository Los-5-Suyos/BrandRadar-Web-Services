package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.aggregates.IncidentEvent;

import java.util.List;

public interface IncidentEventRepository {
    IncidentEvent save(IncidentEvent event);
    List<IncidentEvent> findByIncidentId(Long incidentId);
}