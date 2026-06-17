package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.MentionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SpringDataMentionRepository extends JpaRepository<MentionJpaEntity, Long> {

    List<MentionJpaEntity> findByWorkspaceIdAndPublishedAtBetweenAndIsActiveTrue(
            Long workspaceId, Instant from, Instant to);

    List<MentionJpaEntity> findByWorkspaceIdAndSentimentLabelAndPublishedAtBetweenAndIsActiveTrue(
            Long workspaceId, String sentimentLabel, Instant from, Instant to);

    List<MentionJpaEntity> findTop50ByWorkspaceIdAndSentimentLabelAndPublishedAtBetweenAndIsActiveTrueOrderBySentimentScoreAsc(
            Long workspaceId, String sentimentLabel, Instant from, Instant to);
}
