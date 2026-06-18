package brandradar.crisisdetection.application.commandservices;

import brandradar.crisisdetection.application.commands.CreateCrisisAlertCommand;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;

import java.util.Optional;

public interface CrisisAlertCommandService {
    Optional<CrisisAlert> handle(CreateCrisisAlertCommand command);
}