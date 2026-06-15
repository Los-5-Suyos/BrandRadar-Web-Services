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
                resource.severityLevel(),
                resource.severityLabel()
        );
    }

    public static ReputationIncidentResource toResource(ReputationIncident incident) {
        return new ReputationIncidentResource(
                incident.getId(),
                incident.getBrandId(),
                incident.getMentionStreamId(),
                incident.getSeverityLevel(),
                incident.getSeverityLabel(),
                incident.getStatus(),
                incident.getAssignedTo(),
                incident.getImpactScore(),
                incident.getResolutionSummary(),
                incident.getCreatedAt(),
                incident.getUpdatedAt()
        );
    }
}