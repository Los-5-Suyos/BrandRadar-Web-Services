package brandradar.crisisdetection.infrastructure.persistence.jpa.repositories;

import brandradar.crisisdetection.infrastructure.persistence.jpa.entities.CrisisAnalysisLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataCrisisAnalysisLogRepository extends JpaRepository<CrisisAnalysisLogJpaEntity, Long> {
    List<CrisisAnalysisLogJpaEntity> findByIncidentIdOrderByCreatedAtDesc(Long incidentId);
}