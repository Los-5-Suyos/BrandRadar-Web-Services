package brandradar.infrastructurehealth.interfaces.rest.transform;

import brandradar.infrastructurehealth.application.commands.CreateInfraIncidentCommand;
import brandradar.infrastructurehealth.domain.model.aggregates.InfraIncident;
import brandradar.infrastructurehealth.interfaces.rest.resources.CreateInfraIncidentResource;
import brandradar.infrastructurehealth.interfaces.rest.resources.InfraIncidentResource;

public class InfraIncidentAssembler {

    private InfraIncidentAssembler() {}

    public static CreateInfraIncidentCommand toCommand(CreateInfraIncidentResource resource) {
        return new CreateInfraIncidentCommand(
                resource.serviceHealthCheckId(),
                resource.incidentType(),
                resource.severityLevel(),
                resource.severityLabel()
        );
    }

    public static InfraIncidentResource toResource(InfraIncident incident) {
        return new InfraIncidentResource(
                incident.getId(),
                incident.getServiceHealthCheckId(),
                incident.getIncidentType(),
                incident.getSeverityLevel(),
                incident.getSeverityLabel(),
                incident.getStatus(),
                incident.getEstimatedReputationalImpact(),
                incident.getDetectedAt(),
                incident.getResolvedAt()
        );
    }
}