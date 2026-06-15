package brandradar.infrastructurehealth.application.queryservices;

import brandradar.infrastructurehealth.application.queries.GetInfraIncidentsByStatusQuery;
import brandradar.infrastructurehealth.domain.model.aggregates.InfraIncident;

import java.util.List;
import java.util.Optional;

public interface InfraIncidentQueryService {
    List<InfraIncident> handle(GetInfraIncidentsByStatusQuery query);
    Optional<InfraIncident> findById(Long id);
}