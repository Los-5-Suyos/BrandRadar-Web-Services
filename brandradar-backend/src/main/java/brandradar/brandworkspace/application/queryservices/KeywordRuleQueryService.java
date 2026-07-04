package brandradar.brandworkspace.application.queryservices;

import brandradar.brandworkspace.application.queries.GetKeywordRulesByBrandIdQuery;
import brandradar.brandworkspace.domain.model.aggregates.KeywordRule;

import java.util.List;
import java.util.Optional;

public interface KeywordRuleQueryService {
    List<KeywordRule> handle(GetKeywordRulesByBrandIdQuery query);
    Optional<KeywordRule> findById(Long id);
}