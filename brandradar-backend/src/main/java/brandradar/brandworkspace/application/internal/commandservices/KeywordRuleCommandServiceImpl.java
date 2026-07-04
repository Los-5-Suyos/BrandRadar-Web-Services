package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.CreateKeywordRuleCommand;
import brandradar.brandworkspace.application.commandservices.KeywordRuleCommandService;
import brandradar.brandworkspace.domain.model.aggregates.KeywordRule;
import brandradar.brandworkspace.domain.model.repositories.KeywordRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class KeywordRuleCommandServiceImpl implements KeywordRuleCommandService {

    private final KeywordRuleRepository keywordRuleRepository;

    public KeywordRuleCommandServiceImpl(KeywordRuleRepository keywordRuleRepository) {
        this.keywordRuleRepository = keywordRuleRepository;
    }

    @Override
    @Transactional
    public KeywordRule handle(CreateKeywordRuleCommand command) {
        var rule = KeywordRule.create(command.brandId(), command.keyword(), command.matchType());
        var saved = keywordRuleRepository.save(rule);
        log.info("KeywordRule created with id={} for brandId={}", saved.getId(), command.brandId());
        return saved;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        keywordRuleRepository.deleteById(id);
        log.info("KeywordRule deleted with id={}", id);
    }
}