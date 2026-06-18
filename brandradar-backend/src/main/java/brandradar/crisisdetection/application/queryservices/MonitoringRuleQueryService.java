package brandradar.crisisdetection.application.queryservices;

import brandradar.crisisdetection.application.queries.GetMonitoringRulesByBrandIdQuery;
import brandradar.crisisdetection.domain.model.aggregates.MonitoringRule;

import java.util.List;
import java.util.Optional;

public interface MonitoringRuleQueryService {
    List<MonitoringRule> handle(GetMonitoringRulesByBrandIdQuery query);
    Optional<MonitoringRule> findById(Long id);
}