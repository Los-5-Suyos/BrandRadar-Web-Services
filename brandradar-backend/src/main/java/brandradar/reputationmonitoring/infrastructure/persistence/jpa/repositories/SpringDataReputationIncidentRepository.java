package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationIncidentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataReputationIncidentRepository extends JpaRepository<ReputationIncidentJpaEntity, Long> {
    List<ReputationIncidentJpaEntity> findByBrandId(Long brandId);
    List<ReputationIncidentJpaEntity> findByStatus(String status);
}