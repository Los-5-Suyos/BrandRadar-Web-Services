package brandradar.reputationmonitoring.infrastructure.persistence.jpa;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentLabel;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers.MentionPersistenceMapper;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories.SpringDataMentionRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class MentionPersistenceAdapter implements MentionRepository {

    private final SpringDataMentionRepository springDataRepository;

    public MentionPersistenceAdapter(SpringDataMentionRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Mention save(Mention mention) {
        var jpaEntity = MentionPersistenceMapper.toJpaEntity(mention);
        var saved = springDataRepository.save(jpaEntity);
        return MentionPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Mention> saveAll(List<Mention> mentions) {
        var jpaEntities = mentions.stream().map(MentionPersistenceMapper::toJpaEntity).toList();
        return springDataRepository.saveAll(jpaEntities)
                .stream()
                .map(MentionPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Mention> findById(Long id) {
        return springDataRepository.findById(id)
                .map(MentionPersistenceMapper::toDomain);
    }

    @Override
    public List<Mention> findByWorkspaceIdAndPublishedAtBetweenAndIsActiveTrue(Long workspaceId, Instant from, Instant to) {
        return springDataRepository.findByWorkspaceIdAndPublishedAtBetweenAndIsActiveTrue(workspaceId, from, to)
                .stream()
                .map(MentionPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Mention> findByWorkspaceIdAndSentimentLabelAndPublishedAtBetweenAndIsActiveTrue(
            Long workspaceId, SentimentLabel sentimentLabel, Instant from, Instant to) {
        return springDataRepository.findByWorkspaceIdAndSentimentLabelAndPublishedAtBetweenAndIsActiveTrue(
                        workspaceId, sentimentLabel.name(), from, to)
                .stream()
                .map(MentionPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Mention> findTop50ByWorkspaceIdAndSentimentLabelAndPublishedAtBetweenAndIsActiveTrueOrderBySentimentScoreAsc(
            Long workspaceId, SentimentLabel sentimentLabel, Instant from, Instant to) {
        return springDataRepository.findTop50ByWorkspaceIdAndSentimentLabelAndPublishedAtBetweenAndIsActiveTrueOrderBySentimentScoreAsc(
                        workspaceId, sentimentLabel.name(), from, to)
                .stream()
                .map(MentionPersistenceMapper::toDomain)
                .toList();
    }
}
