package brandradar.crisisdetection.interfaces.rest.transform;

import brandradar.crisisdetection.application.commands.CreateMonitoringRuleCommand;
import brandradar.crisisdetection.domain.model.aggregates.MonitoringRule;
import brandradar.crisisdetection.interfaces.rest.resources.CreateMonitoringRuleResource;
import brandradar.crisisdetection.interfaces.rest.resources.MonitoringRuleResource;

public class MonitoringRuleAssembler {

    private MonitoringRuleAssembler() {}

    public static CreateMonitoringRuleCommand toCommand(CreateMonitoringRuleResource resource) {
        return new CreateMonitoringRuleCommand(
                resource.brandId(),
                resource.name(),
                resource.thresholdMentionVolumeLimit(),
                resource.thresholdNegativeSentimentPct(),
                resource.thresholdTimeWindowMinutes(),
                resource.notifCooldownMinutes()
        );
    }

    public static MonitoringRuleResource toResource(MonitoringRule rule) {
        return new MonitoringRuleResource(
                rule.getId(),
                rule.getBrandId(),
                rule.getName(),
                rule.getIsActive(),
                rule.getThresholdMentionVolumeLimit(),
                rule.getThresholdNegativeSentimentPct(),
                rule.getThresholdTimeWindowMinutes(),
                rule.getNotifCooldownMinutes(),
                rule.getCreatedAt(),
                rule.getUpdatedAt()
        );
    }
}