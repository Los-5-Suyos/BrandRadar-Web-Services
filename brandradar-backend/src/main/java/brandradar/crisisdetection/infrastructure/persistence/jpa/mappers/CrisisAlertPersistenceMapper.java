package brandradar.crisisdetection.infrastructure.persistence.jpa.mappers;

import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.infrastructure.persistence.jpa.entities.CrisisAlertJpaEntity;

public class CrisisAlertPersistenceMapper {

    private CrisisAlertPersistenceMapper() {}

    public static CrisisAlertJpaEntity toJpaEntity(CrisisAlert alert) {
        return new CrisisAlertJpaEntity(
                alert.getId(),
                alert.getBrandId(),
                alert.getMentionStreamId(),
                alert.getMonitoringRuleId(),
                alert.getPriorityLevel(),
                alert.getPriorityLabel(),
                alert.getTitle(),
                alert.getDescription(),
                alert.getStatus(),
                alert.getTriggerType(),
                alert.getTriggerDeviationPct(),
                alert.getTriggerConfidence(),
                alert.getDetectedAt(),
                alert.getAcknowledgedAt(),
                alert.getDismissedReason(),
                alert.getResponseTimeMinutes()
        );
    }

    public static CrisisAlert toDomain(CrisisAlertJpaEntity entity) {
        return CrisisAlert.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getMentionStreamId(),
                entity.getMonitoringRuleId(),
                entity.getPriorityLevel(),
                entity.getPriorityLabel(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getTriggerType(),
                entity.getTriggerDeviationPct(),
                entity.getTriggerConfidence(),
                entity.getDetectedAt(),
                entity.getAcknowledgedAt(),
                entity.getDismissedReason(),
                entity.getResponseTimeMinutes()
        );
    }
}