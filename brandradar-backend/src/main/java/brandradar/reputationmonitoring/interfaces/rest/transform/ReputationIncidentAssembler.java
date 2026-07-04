package brandradar.reputationmonitoring.interfaces.rest.transform;

import brandradar.reputationmonitoring.application.commands.CreateReputationIncidentCommand;
import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;
import brandradar.reputationmonitoring.interfaces.rest.resources.CreateReputationIncidentResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.ReputationIncidentResource;

public class ReputationIncidentAssembler {

    private ReputationIncidentAssembler() {}

    public static CreateReputationIncidentCommand toCommand(CreateReputationIncidentResource resource) {
        return new CreateReputationIncidentCommand(
                resource.brandId(),
                resource.mentionStreamId(),
                resource.crisisAlertId(),
                resource.severityLevel(),
                resource.severityLabel(),
                resource.title(),
                resource.description()
        );
    }

    public static ReputationIncidentResource toResource(ReputationIncident incident) {
        return new ReputationIncidentResource(
                incident.getId(),
                incident.getBrandId(),
                incident.getMentionStreamId(),
                incident.getCrisisAlertId(),
                incident.getSeverityLevel(),
                incident.getSeverityLabel(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getStatus(),
                incident.getProgressPct(),
                incident.getAssignedTo(),
                incident.getImpactScore(),
                incident.getResolutionSummary(),
                incident.getResolvedAt(),
                incident.getCreatedAt(),
                incident.getUpdatedAt()
        );
    }
}