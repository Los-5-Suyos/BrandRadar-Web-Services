package brandradar.crisisdetection.infrastructure.persistence.jpa.repositories;

import brandradar.crisisdetection.infrastructure.persistence.jpa.entities.AlertPreferenceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataAlertPreferenceRepository extends JpaRepository<AlertPreferenceJpaEntity, Long> {
    Optional<AlertPreferenceJpaEntity> findByBrandIdAndKey(Long brandId, String key);
    List<AlertPreferenceJpaEntity> findByBrandId(Long brandId);
}