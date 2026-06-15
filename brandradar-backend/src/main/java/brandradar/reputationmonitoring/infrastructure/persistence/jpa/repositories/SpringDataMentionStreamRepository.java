package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.MentionStreamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataMentionStreamRepository extends JpaRepository<MentionStreamJpaEntity, Long> {
    List<MentionStreamJpaEntity> findByBrandId(Long brandId);
}