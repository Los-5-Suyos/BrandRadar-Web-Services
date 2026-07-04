package brandradar.crisisdetection.infrastructure.persistence.jpa;

import brandradar.crisisdetection.domain.model.aggregates.AlertPreference;
import brandradar.crisisdetection.domain.model.repositories.AlertPreferenceRepository;
import brandradar.crisisdetection.infrastructure.persistence.jpa.mappers.AlertPreferencePersistenceMapper;
import brandradar.crisisdetection.infrastructure.persistence.jpa.repositories.SpringDataAlertPreferenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlertPreferencePersistenceAdapter implements AlertPreferenceRepository {

    private final SpringDataAlertPreferenceRepository springDataRepository;

    public AlertPreferencePersistenceAdapter(SpringDataAlertPreferenceRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public AlertPreference save(AlertPreference preference) {
        var jpaEntity = AlertPreferencePersistenceMapper.toJpaEntity(preference);
        var saved = springDataRepository.save(jpaEntity);
        return AlertPreferencePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<AlertPreference> findByBrandIdAndKey(Long brandId, String key) {
        return springDataRepository.findByBrandIdAndKey(brandId, key)
                .map(AlertPreferencePersistenceMapper::toDomain);
    }

    @Override
    public List<AlertPreference> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(AlertPreferencePersistenceMapper::toDomain)
                .toList();
    }
}