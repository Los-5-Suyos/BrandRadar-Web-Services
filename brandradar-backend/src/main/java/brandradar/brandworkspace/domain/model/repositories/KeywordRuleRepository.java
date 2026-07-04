package brandradar.brandworkspace.domain.model.repositories;

import brandradar.brandworkspace.domain.model.aggregates.KeywordRule;

import java.util.List;
import java.util.Optional;

public interface KeywordRuleRepository {
    KeywordRule save(KeywordRule rule);
    Optional<KeywordRule> findById(Long id);
    List<KeywordRule> findByBrandId(Long brandId);
    void deleteById(Long id);
}