package brandradar.infrastructurehealth.application.queryservices;

import brandradar.infrastructurehealth.application.queries.GetAllHealthChecksQuery;
import brandradar.infrastructurehealth.domain.model.aggregates.ServiceHealthCheck;

import java.util.List;
import java.util.Optional;

public interface ServiceHealthCheckQueryService {
    List<ServiceHealthCheck> handle(GetAllHealthChecksQuery query);
    Optional<ServiceHealthCheck> findById(Long id);
}