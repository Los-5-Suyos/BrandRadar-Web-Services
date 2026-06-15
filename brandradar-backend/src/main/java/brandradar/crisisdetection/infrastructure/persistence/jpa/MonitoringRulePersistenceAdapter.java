package brandradar.crisisdetection.infrastructure.persistence.jpa;

import brandradar.crisisdetection.domain.model.aggregates.MonitoringRule;
import brandradar.crisisdetection.domain.model.repositories.MonitoringRuleRepository;
import brandradar.crisisdetection.infrastructure.persistence.jpa.mappers.MonitoringRulePersistenceMapper;
import brandradar.crisisdetection.infrastructure.persistence.jpa.repositories.SpringDataMonitoringRuleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MonitoringRulePersistenceAdapter implements MonitoringRuleRepository {

    private final SpringDataMonitoringRuleRepository springDataRepository;

    public MonitoringRulePersistenceAdapter(SpringDataMonitoringRuleRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public MonitoringRule save(MonitoringRule rule) {
        var jpaEntity = MonitoringRulePersistenceMapper.toJpaEntity(rule);
        var saved = springDataRepository.save(jpaEntity);
        return MonitoringRulePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<MonitoringRule> findById(Long id) {
        return springDataRepository.findById(id)
                .map(MonitoringRulePersistenceMapper::toDomain);
    }

    @Override
    public List<MonitoringRule> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(MonitoringRulePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<MonitoringRule> findByBrandIdAndIsActive(Long brandId, Boolean isActive) {
        return springDataRepository.findByBrandIdAndIsActive(brandId, isActive)
                .stream()
                .map(MonitoringRulePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
}