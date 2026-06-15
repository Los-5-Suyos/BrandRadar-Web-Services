package brandradar.infrastructurehealth.application.commandservices;

import brandradar.infrastructurehealth.application.commands.CreateServiceHealthCheckCommand;
import brandradar.infrastructurehealth.domain.model.aggregates.ServiceHealthCheck;

import java.util.Optional;

public interface ServiceHealthCheckCommandService {
    Optional<ServiceHealthCheck> handle(CreateServiceHealthCheckCommand command);
}