package brandradar.crisisdetection.application.commandservices;

import brandradar.crisisdetection.application.commands.CreateMonitoringRuleCommand;
import brandradar.crisisdetection.domain.model.aggregates.MonitoringRule;

import java.util.Optional;

public interface MonitoringRuleCommandService {
    Optional<MonitoringRule> handle(CreateMonitoringRuleCommand command);
}