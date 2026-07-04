package brandradar.brandworkspace.application.internal.queryservices;

import brandradar.brandworkspace.application.queries.GetKeywordRulesByBrandIdQuery;
import brandradar.brandworkspace.application.queryservices.KeywordRuleQueryService;
import brandradar.brandworkspace.domain.model.aggregates.KeywordRule;
import brandradar.brandworkspace.domain.model.repositories.KeywordRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeywordRuleQueryServiceImpl implements KeywordRuleQueryService {

    private final KeywordRuleRepository keywordRuleRepository;

    public KeywordRuleQueryServiceImpl(KeywordRuleRepository keywordRuleRepository) {
        this.keywordRuleRepository = keywordRuleRepository;
    }

    @Override
    public List<KeywordRule> handle(GetKeywordRulesByBrandIdQuery query) {
        return keywordRuleRepository.findByBrandId(query.brandId());
    }

    @Override
    public Optional<KeywordRule> findById(Long id) {
        return keywordRuleRepository.findById(id);
    }
}