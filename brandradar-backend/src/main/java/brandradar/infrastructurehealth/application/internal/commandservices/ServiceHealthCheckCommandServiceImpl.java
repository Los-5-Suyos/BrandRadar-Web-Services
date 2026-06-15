package brandradar.infrastructurehealth.application.internal.commandservices;

import brandradar.infrastructurehealth.application.commands.CreateServiceHealthCheckCommand;
import brandradar.infrastructurehealth.application.commandservices.ServiceHealthCheckCommandService;
import brandradar.infrastructurehealth.domain.model.aggregates.ServiceHealthCheck;
import brandradar.infrastructurehealth.domain.model.repositories.ServiceHealthCheckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class ServiceHealthCheckCommandServiceImpl implements ServiceHealthCheckCommandService {

    private final ServiceHealthCheckRepository serviceHealthCheckRepository;

    public ServiceHealthCheckCommandServiceImpl(ServiceHealthCheckRepository serviceHealthCheckRepository) {
        this.serviceHealthCheckRepository = serviceHealthCheckRepository;
    }

    @Override
    @Transactional
    public Optional<ServiceHealthCheck> handle(CreateServiceHealthCheckCommand command) {
        var healthCheck = ServiceHealthCheck.create(command.serviceName(), command.endpointUrl(),
                command.endpointMethod(), command.endpointTimeoutMs());
        var saved = serviceHealthCheckRepository.save(healthCheck);
        log.info("ServiceHealthCheck created with id={}", saved.getId());
        return Optional.of(saved);
    }
}