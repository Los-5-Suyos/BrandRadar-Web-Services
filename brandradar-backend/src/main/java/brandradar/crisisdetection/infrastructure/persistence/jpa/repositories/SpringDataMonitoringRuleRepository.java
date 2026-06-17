package brandradar.crisisdetection.infrastructure.persistence.jpa.repositories;

import brandradar.crisisdetection.infrastructure.persistence.jpa.entities.MonitoringRuleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataMonitoringRuleRepository extends JpaRepository<MonitoringRuleJpaEntity, Long> {
    List<MonitoringRuleJpaEntity> findByBrandId(Long brandId);
    List<MonitoringRuleJpaEntity> findByBrandIdAndIsActive(Long brandId, Boolean isActive);
}