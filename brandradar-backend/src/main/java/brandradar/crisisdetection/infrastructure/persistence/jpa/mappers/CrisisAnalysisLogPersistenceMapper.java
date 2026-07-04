package brandradar.crisisdetection.infrastructure.persistence.jpa.mappers;

import brandradar.crisisdetection.domain.model.aggregates.CrisisAnalysisLog;
import brandradar.crisisdetection.infrastructure.persistence.jpa.entities.CrisisAnalysisLogJpaEntity;

public class CrisisAnalysisLogPersistenceMapper {

    private CrisisAnalysisLogPersistenceMapper() {}

    public static CrisisAnalysisLogJpaEntity toJpaEntity(CrisisAnalysisLog log) {
        return new CrisisAnalysisLogJpaEntity(
                log.getId(),
                log.getIncidentId(),
                log.getPattern(),
                log.getKeywords(),
                log.getGeofocus(),
                log.getDiagnostico(),
                log.getAccion()
        );
    }

    public static CrisisAnalysisLog toDomain(CrisisAnalysisLogJpaEntity entity) {
        return CrisisAnalysisLog.rehydrate(
                entity.getId(),
                entity.getIncidentId(),
                entity.getPattern(),
                entity.getKeywords(),
                entity.getGeofocus(),
                entity.getDiagnostico(),
                entity.getAccion(),
                entity.getCreatedAt()
        );
    }
}