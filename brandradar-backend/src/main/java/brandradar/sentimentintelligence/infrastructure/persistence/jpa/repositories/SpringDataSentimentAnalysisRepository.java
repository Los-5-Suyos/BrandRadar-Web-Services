package brandradar.sentimentintelligence.infrastructure.persistence.jpa.repositories;

import brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities.SentimentAnalysisJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataSentimentAnalysisRepository extends JpaRepository<SentimentAnalysisJpaEntity, Long> {
    List<SentimentAnalysisJpaEntity> findByBrandId(Long brandId);
}