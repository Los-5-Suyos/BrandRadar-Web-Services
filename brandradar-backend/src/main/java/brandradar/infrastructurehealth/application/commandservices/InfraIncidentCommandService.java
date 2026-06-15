package brandradar.infrastructurehealth.application.commandservices;

import brandradar.infrastructurehealth.application.commands.CreateInfraIncidentCommand;
import brandradar.infrastructurehealth.domain.model.aggregates.InfraIncident;

import java.util.Optional;

public interface InfraIncidentCommandService {
    Optional<InfraIncident> handle(CreateInfraIncidentCommand command);
}