package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.MentionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataMentionRepository extends JpaRepository<MentionJpaEntity, Long> {
    List<MentionJpaEntity> findByBrandId(Long brandId);
    List<MentionJpaEntity> findByMentionStreamId(Long mentionStreamId);
    boolean existsBySourceUrl(String sourceUrl);
}