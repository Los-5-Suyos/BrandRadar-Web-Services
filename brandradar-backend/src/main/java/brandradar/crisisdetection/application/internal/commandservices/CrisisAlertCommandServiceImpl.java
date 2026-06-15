package brandradar.crisisdetection.application.internal.commandservices;

import brandradar.crisisdetection.application.commands.CreateCrisisAlertCommand;
import brandradar.crisisdetection.application.commandservices.CrisisAlertCommandService;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class CrisisAlertCommandServiceImpl implements CrisisAlertCommandService {

    private final CrisisAlertRepository crisisAlertRepository;

    public CrisisAlertCommandServiceImpl(CrisisAlertRepository crisisAlertRepository) {
        this.crisisAlertRepository = crisisAlertRepository;
    }

    @Override
    @Transactional
    public Optional<CrisisAlert> handle(CreateCrisisAlertCommand command) {
        var alert = CrisisAlert.create(command.brandId(), command.mentionStreamId(),
                command.monitoringRuleId(), command.priorityLevel(), command.priorityLabel(),
                command.triggerType(), command.triggerDeviationPct(), command.triggerConfidence());
        var saved = crisisAlertRepository.save(alert);
        log.info("CrisisAlert created with id={}", saved.getId());
        return Optional.of(saved);
    }
}