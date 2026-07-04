package brandradar.crisisdetection.infrastructure.persistence.jpa.mappers;

import brandradar.crisisdetection.domain.model.aggregates.AlertPreference;
import brandradar.crisisdetection.infrastructure.persistence.jpa.entities.AlertPreferenceJpaEntity;

public class AlertPreferencePersistenceMapper {

    private AlertPreferencePersistenceMapper() {}

    public static AlertPreferenceJpaEntity toJpaEntity(AlertPreference preference) {
        return new AlertPreferenceJpaEntity(
                preference.getId(),
                preference.getBrandId(),
                preference.getKey(),
                preference.getEnabled()
        );
    }

    public static AlertPreference toDomain(AlertPreferenceJpaEntity entity) {
        return AlertPreference.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getKey(),
                entity.getEnabled()
        );
    }
}