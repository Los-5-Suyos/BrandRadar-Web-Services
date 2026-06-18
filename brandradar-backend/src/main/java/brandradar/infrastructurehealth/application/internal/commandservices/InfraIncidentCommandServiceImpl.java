package brandradar.infrastructurehealth.application.internal.commandservices;

import brandradar.infrastructurehealth.application.commands.CreateInfraIncidentCommand;
import brandradar.infrastructurehealth.application.commandservices.InfraIncidentCommandService;
import brandradar.infrastructurehealth.domain.model.aggregates.InfraIncident;
import brandradar.infrastructurehealth.domain.model.repositories.InfraIncidentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class InfraIncidentCommandServiceImpl implements InfraIncidentCommandService {

    private final InfraIncidentRepository infraIncidentRepository;

    public InfraIncidentCommandServiceImpl(InfraIncidentRepository infraIncidentRepository) {
        this.infraIncidentRepository = infraIncidentRepository;
    }

    @Override
    @Transactional
    public Optional<InfraIncident> handle(CreateInfraIncidentCommand command) {
        var incident = InfraIncident.create(command.serviceHealthCheckId(), command.incidentType(),
                command.severityLevel(), command.severityLabel());
        var saved = infraIncidentRepository.save(incident);
        log.info("InfraIncident created with id={}", saved.getId());
        return Optional.of(saved);
    }
}