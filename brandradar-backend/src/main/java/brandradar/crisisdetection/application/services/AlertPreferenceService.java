package brandradar.crisisdetection.application.services;

import brandradar.crisisdetection.domain.model.aggregates.AlertPreference;
import brandradar.crisisdetection.domain.model.repositories.AlertPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AlertPreferenceService {

    public static final String SCORE_DROP = "SCORE_DROP";
    public static final String NEGATIVE_SPIKE = "NEGATIVE_SPIKE";
    public static final String CRITICAL_KEYWORD = "CRITICAL_KEYWORD";
    public static final String NEW_INCIDENT = "NEW_INCIDENT";
    public static final String HIGH_VOLUME = "HIGH_VOLUME";

    private static final List<String> ALL_KEYS = List.of(
            SCORE_DROP, NEGATIVE_SPIKE, CRITICAL_KEYWORD, NEW_INCIDENT, HIGH_VOLUME);

    private final AlertPreferenceRepository repository;

    public AlertPreferenceService(AlertPreferenceRepository repository) {
        this.repository = repository;
    }

    public List<AlertPreference> getAllForBrand(Long brandId) {
        var existing = repository.findByBrandId(brandId);
        var existingKeys = existing.stream().map(AlertPreference::getKey).toList();

        var missing = ALL_KEYS.stream()
                .filter(key -> !existingKeys.contains(key))
                .map(key -> AlertPreference.create(brandId, key, true))
                .toList();

        var all = new java.util.ArrayList<>(existing);
        all.addAll(missing);
        return all;
    }

    @Transactional
    public AlertPreference updatePreference(Long brandId, String key, Boolean enabled) {
        var existing = repository.findByBrandIdAndKey(brandId, key);
        var toSave = existing.isPresent()
                ? existing.get().withEnabled(enabled)
                : AlertPreference.create(brandId, key, enabled);
        return repository.save(toSave);
    }

    public boolean isEnabled(Long brandId, String key) {
        return repository.findByBrandIdAndKey(brandId, key)
                .map(AlertPreference::getEnabled)
                .orElse(true); // default: activado si nunca se configuró
    }
}