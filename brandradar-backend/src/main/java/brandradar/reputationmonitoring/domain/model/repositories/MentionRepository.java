package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentLabel;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MentionRepository {
    Mention save(Mention mention);
    List<Mention> saveAll(List<Mention> mentions);
    Optional<Mention> findById(Long id);

    List<Mention> findByWorkspaceIdAndPublishedAtBetweenAndIsActiveTrue(
            Long workspaceId, Instant from, Instant to);

    List<Mention> findByWorkspaceIdAndSentimentLabelAndPublishedAtBetweenAndIsActiveTrue(
            Long workspaceId, SentimentLabel sentimentLabel, Instant from, Instant to);

    List<Mention> findTop50ByWorkspaceIdAndSentimentLabelAndPublishedAtBetweenAndIsActiveTrueOrderBySentimentScoreAsc(
            Long workspaceId, SentimentLabel sentimentLabel, Instant from, Instant to);
}
