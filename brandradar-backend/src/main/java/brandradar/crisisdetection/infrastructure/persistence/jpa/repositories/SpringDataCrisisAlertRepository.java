package brandradar.crisisdetection.infrastructure.persistence.jpa.repositories;

import brandradar.crisisdetection.infrastructure.persistence.jpa.entities.CrisisAlertJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataCrisisAlertRepository extends JpaRepository<CrisisAlertJpaEntity, Long> {
    List<CrisisAlertJpaEntity> findByBrandId(Long brandId);
    List<CrisisAlertJpaEntity> findByStatus(String status);
}