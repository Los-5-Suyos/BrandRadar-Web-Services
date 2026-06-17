package brandradar.sentimentintelligence.infrastructure.persistence.jpa.repositories;

import brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities.SentimentResultJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataSentimentResultRepository extends JpaRepository<SentimentResultJpaEntity, Long> {
    List<SentimentResultJpaEntity> findBySentimentAnalysisId(Long sentimentAnalysisId);
}