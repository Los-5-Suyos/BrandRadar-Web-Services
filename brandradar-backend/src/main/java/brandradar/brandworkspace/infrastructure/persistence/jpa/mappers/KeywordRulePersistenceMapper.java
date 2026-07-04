package brandradar.brandworkspace.infrastructure.persistence.jpa.mappers;

import brandradar.brandworkspace.domain.model.aggregates.KeywordRule;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.KeywordRuleJpaEntity;

public class KeywordRulePersistenceMapper {

    private KeywordRulePersistenceMapper() {}

    public static KeywordRuleJpaEntity toJpaEntity(KeywordRule rule) {
        return new KeywordRuleJpaEntity(
                rule.getId(),
                rule.getBrandId(),
                rule.getKeyword(),
                rule.getMatchType(),
                rule.getWeight(),
                rule.getIsActive()
        );
    }

    public static KeywordRule toDomain(KeywordRuleJpaEntity entity) {
        return KeywordRule.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getKeyword(),
                entity.getMatchType(),
                entity.getWeight(),
                entity.getIsActive()
        );
    }
}