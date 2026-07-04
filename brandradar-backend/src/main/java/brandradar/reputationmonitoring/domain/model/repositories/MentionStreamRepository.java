package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.aggregates.MentionStream;

import java.util.List;
import java.util.Optional;

public interface MentionStreamRepository {
    MentionStream save(MentionStream mentionStream);
    Optional<MentionStream> findById(Long id);
    List<MentionStream> findByBrandId(Long brandId);
    void deleteById(Long id);
}