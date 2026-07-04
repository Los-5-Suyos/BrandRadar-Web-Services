package brandradar.crisisdetection.domain.model.repositories;

import brandradar.crisisdetection.domain.model.aggregates.AlertPreference;

import java.util.List;
import java.util.Optional;

public interface AlertPreferenceRepository {
    AlertPreference save(AlertPreference preference);
    Optional<AlertPreference> findByBrandIdAndKey(Long brandId, String key);
    List<AlertPreference> findByBrandId(Long brandId);
}