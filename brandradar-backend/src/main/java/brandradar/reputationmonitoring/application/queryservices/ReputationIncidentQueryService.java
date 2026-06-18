package brandradar.reputationmonitoring.application.queryservices;

import brandradar.reputationmonitoring.application.queries.GetIncidentsByBrandIdQuery;
import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;

import java.util.List;
import java.util.Optional;

public interface ReputationIncidentQueryService {
    List<ReputationIncident> handle(GetIncidentsByBrandIdQuery query);
    Optional<ReputationIncident> findById(Long id);
}