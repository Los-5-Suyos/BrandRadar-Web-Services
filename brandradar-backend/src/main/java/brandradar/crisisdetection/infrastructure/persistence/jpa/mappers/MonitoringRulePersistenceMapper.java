package brandradar.crisisdetection.infrastructure.persistence.jpa.mappers;

import brandradar.crisisdetection.domain.model.aggregates.MonitoringRule;
import brandradar.crisisdetection.infrastructure.persistence.jpa.entities.MonitoringRuleJpaEntity;

public class MonitoringRulePersistenceMapper {

    private MonitoringRulePersistenceMapper() {}

    public static MonitoringRuleJpaEntity toJpaEntity(MonitoringRule rule) {
        return new MonitoringRuleJpaEntity(
                rule.getId(),
                rule.getBrandId(),
                rule.getName(),
                rule.getIsActive(),
                rule.getThresholdMentionVolumeLimit(),
                rule.getThresholdNegativeSentimentPct(),
                rule.getThresholdTimeWindowMinutes(),
                rule.getNotifCooldownMinutes()
        );
    }

    public static MonitoringRule toDomain(MonitoringRuleJpaEntity entity) {
        return MonitoringRule.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getName(),
                entity.getIsActive(),
                entity.getThresholdMentionVolumeLimit(),
                entity.getThresholdNegativeSentimentPct(),
                entity.getThresholdTimeWindowMinutes(),
                entity.getNotifCooldownMinutes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}