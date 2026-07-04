package brandradar.crisisdetection.application.internal.commandservices;

import brandradar.crisisdetection.application.commands.CreateMonitoringRuleCommand;
import brandradar.crisisdetection.application.commandservices.MonitoringRuleCommandService;
import brandradar.crisisdetection.domain.model.aggregates.MonitoringRule;
import brandradar.crisisdetection.domain.model.repositories.MonitoringRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class MonitoringRuleCommandServiceImpl implements MonitoringRuleCommandService {

    private final MonitoringRuleRepository monitoringRuleRepository;

    public MonitoringRuleCommandServiceImpl(MonitoringRuleRepository monitoringRuleRepository) {
        this.monitoringRuleRepository = monitoringRuleRepository;
    }

    @Override
    @Transactional
    public Optional<MonitoringRule> handle(CreateMonitoringRuleCommand command) {
        var rule = MonitoringRule.create(command.brandId(), command.name(),
                command.thresholdMentionVolumeLimit(), command.thresholdNegativeSentimentPct(),
                command.thresholdTimeWindowMinutes(), command.notifCooldownMinutes());
        var saved = monitoringRuleRepository.save(rule);
        log.info("MonitoringRule created with id={}", saved.getId());
        return Optional.of(saved);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        monitoringRuleRepository.deleteById(id);
        log.info("MonitoringRule deleted with id={}", id);
    }
}