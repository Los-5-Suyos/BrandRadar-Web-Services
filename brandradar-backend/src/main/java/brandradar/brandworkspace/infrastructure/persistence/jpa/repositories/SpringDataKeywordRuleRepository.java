package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.KeywordRuleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataKeywordRuleRepository extends JpaRepository<KeywordRuleJpaEntity, Long> {
    List<KeywordRuleJpaEntity> findByBrandIdAndIsActiveTrue(Long brandId);
    List<KeywordRuleJpaEntity> findByBrandId(Long brandId);
}