package brandradar.reputationmonitoring.application.internal.commandservices;

import brandradar.reputationmonitoring.application.commands.CreateReputationIncidentCommand;
import brandradar.reputationmonitoring.application.commandservices.ReputationIncidentCommandService;
import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationIncidentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class ReputationIncidentCommandServiceImpl implements ReputationIncidentCommandService {

    private final ReputationIncidentRepository reputationIncidentRepository;

    public ReputationIncidentCommandServiceImpl(ReputationIncidentRepository reputationIncidentRepository) {
        this.reputationIncidentRepository = reputationIncidentRepository;
    }

    @Override
    @Transactional
    public Optional<ReputationIncident> handle(CreateReputationIncidentCommand command) {
        var incident = ReputationIncident.create(command.brandId(), command.mentionStreamId(),
                command.severityLevel(), command.severityLabel());
        var saved = reputationIncidentRepository.save(incident);
        log.info("ReputationIncident created with id={}", saved.getId());
        return Optional.of(saved);
    }
}