package brandradar.crisisdetection.domain.model.repositories;

import brandradar.crisisdetection.domain.model.aggregates.MonitoringRule;

import java.util.List;
import java.util.Optional;

public interface MonitoringRuleRepository {
    MonitoringRule save(MonitoringRule rule);
    Optional<MonitoringRule> findById(Long id);
    List<MonitoringRule> findByBrandId(Long brandId);
    List<MonitoringRule> findByBrandIdAndIsActive(Long brandId, Boolean isActive);
    void deleteById(Long id);
}