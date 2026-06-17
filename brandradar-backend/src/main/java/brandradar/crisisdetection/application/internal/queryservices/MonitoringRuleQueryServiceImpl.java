package brandradar.crisisdetection.application.internal.queryservices;

import brandradar.crisisdetection.application.queries.GetMonitoringRulesByBrandIdQuery;
import brandradar.crisisdetection.application.queryservices.MonitoringRuleQueryService;
import brandradar.crisisdetection.domain.model.aggregates.MonitoringRule;
import brandradar.crisisdetection.domain.model.repositories.MonitoringRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonitoringRuleQueryServiceImpl implements MonitoringRuleQueryService {

    private final MonitoringRuleRepository monitoringRuleRepository;

    public MonitoringRuleQueryServiceImpl(MonitoringRuleRepository monitoringRuleRepository) {
        this.monitoringRuleRepository = monitoringRuleRepository;
    }

    @Override
    public List<MonitoringRule> handle(GetMonitoringRulesByBrandIdQuery query) {
        return monitoringRuleRepository.findByBrandId(query.brandId());
    }

    @Override
    public Optional<MonitoringRule> findById(Long id) {
        return monitoringRuleRepository.findById(id);
    }
}