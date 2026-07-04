package brandradar.brandworkspace.infrastructure.persistence.jpa;

import brandradar.brandworkspace.domain.model.aggregates.KeywordRule;
import brandradar.brandworkspace.domain.model.repositories.KeywordRuleRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.mappers.KeywordRulePersistenceMapper;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataKeywordRuleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class KeywordRulePersistenceAdapter implements KeywordRuleRepository {

    private final SpringDataKeywordRuleRepository springDataRepository;

    public KeywordRulePersistenceAdapter(SpringDataKeywordRuleRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public KeywordRule save(KeywordRule rule) {
        var jpaEntity = KeywordRulePersistenceMapper.toJpaEntity(rule);
        var saved = springDataRepository.save(jpaEntity);
        return KeywordRulePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<KeywordRule> findById(Long id) {
        return springDataRepository.findById(id)
                .map(KeywordRulePersistenceMapper::toDomain);
    }

    @Override
    public List<KeywordRule> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(KeywordRulePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
}