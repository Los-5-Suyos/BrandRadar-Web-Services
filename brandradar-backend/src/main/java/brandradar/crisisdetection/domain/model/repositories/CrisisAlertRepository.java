package brandradar.crisisdetection.domain.model.repositories;

import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;

import java.util.List;
import java.util.Optional;

public interface CrisisAlertRepository {
    CrisisAlert save(CrisisAlert alert);
    Optional<CrisisAlert> findById(Long id);
    List<CrisisAlert> findByBrandId(Long brandId);
    List<CrisisAlert> findByStatus(String status);
}