package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.IncidentEventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataIncidentEventRepository extends JpaRepository<IncidentEventJpaEntity, Long> {
    List<IncidentEventJpaEntity> findByIncidentId(Long incidentId);
}