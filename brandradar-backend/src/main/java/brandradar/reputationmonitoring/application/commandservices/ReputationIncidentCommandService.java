package brandradar.reputationmonitoring.application.commandservices;

import brandradar.reputationmonitoring.application.commands.CreateReputationIncidentCommand;
import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;

import java.util.Optional;

public interface ReputationIncidentCommandService {
    Optional<ReputationIncident> handle(CreateReputationIncidentCommand command);
    ReputationIncident updateStatus(ReputationIncident incident);
}