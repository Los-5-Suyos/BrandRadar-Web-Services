package brandradar.brandworkspace.application.commandservices;

import brandradar.brandworkspace.application.commands.CreateKeywordRuleCommand;
import brandradar.brandworkspace.domain.model.aggregates.KeywordRule;

public interface KeywordRuleCommandService {
    KeywordRule handle(CreateKeywordRuleCommand command);
    void deleteById(Long id);
}