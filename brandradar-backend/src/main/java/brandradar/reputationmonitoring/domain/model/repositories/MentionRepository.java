package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;

import java.util.List;
import java.util.Optional;

public interface MentionRepository {
    Mention save(Mention mention);
    Optional<Mention> findById(Long id);
    List<Mention> findByBrandId(Long brandId);
    List<Mention> findByMentionStreamId(Long mentionStreamId);
}