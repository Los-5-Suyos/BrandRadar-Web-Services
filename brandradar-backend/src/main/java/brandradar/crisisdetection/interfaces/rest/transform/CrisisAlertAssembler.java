package brandradar.crisisdetection.interfaces.rest.transform;

import brandradar.crisisdetection.application.commands.CreateCrisisAlertCommand;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.interfaces.rest.resources.CreateCrisisAlertResource;
import brandradar.crisisdetection.interfaces.rest.resources.CrisisAlertResource;

public class CrisisAlertAssembler {

    private CrisisAlertAssembler() {}

    public static CreateCrisisAlertCommand toCommand(CreateCrisisAlertResource resource) {
        return new CreateCrisisAlertCommand(
                resource.brandId(),
                resource.mentionStreamId(),
                resource.monitoringRuleId(),
                resource.priorityLevel(),
                resource.priorityLabel(),
                resource.triggerType(),
                resource.triggerDeviationPct(),
                resource.triggerConfidence()
        );
    }

    public static CrisisAlertResource toResource(CrisisAlert alert) {
        return new CrisisAlertResource(
                alert.getId(),
                alert.getBrandId(),
                alert.getMentionStreamId(),
                alert.getMonitoringRuleId(),
                alert.getPriorityLevel(),
                alert.getPriorityLabel(),
                alert.getStatus(),
                alert.getTriggerType(),
                alert.getTriggerDeviationPct(),
                alert.getTriggerConfidence(),
                alert.getDetectedAt(),
                alert.getAcknowledgedAt()
        );
    }
}