package brandradar.crisisdetection.application.queryservices;

import brandradar.crisisdetection.application.queries.GetCrisisAlertsByBrandIdQuery;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;

import java.util.List;
import java.util.Optional;

public interface CrisisAlertQueryService {
    List<CrisisAlert> handle(GetCrisisAlertsByBrandIdQuery query);
    Optional<CrisisAlert> findById(Long id);
}