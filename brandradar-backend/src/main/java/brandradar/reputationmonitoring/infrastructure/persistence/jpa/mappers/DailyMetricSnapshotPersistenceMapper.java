package brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers;

import brandradar.reputationmonitoring.domain.model.aggregates.DailyMetricSnapshot;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentIndex;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentScoreLabel;
import brandradar.reputationmonitoring.domain.model.valueobjects.SourceType;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.DailyMetricSnapshotJpaEntity;

public class DailyMetricSnapshotPersistenceMapper {

    private DailyMetricSnapshotPersistenceMapper() {}

    public static DailyMetricSnapshotJpaEntity toJpaEntity(DailyMetricSnapshot snapshot) {
        return new DailyMetricSnapshotJpaEntity(
                snapshot.getId(),
                snapshot.getWorkspaceId(),
                snapshot.getSnapshotDate(),
                snapshot.getSentimentScore(),
                snapshot.getSentimentScoreLabel().name(),
                snapshot.getTotalMentions(),
                snapshot.getNegativeCount(),
                snapshot.getNeutralCount(),
                snapshot.getPositiveCount(),
                snapshot.getNegativePercent(),
                snapshot.getNeutralPercent(),
                snapshot.getPositivePercent(),
                snapshot.getVariationVsYesterday(),
                snapshot.getTopSource().name(),
                snapshot.getTopSourceSentimentIndex().name(),
                snapshot.getCalculatedAt()
        );
    }

    public static DailyMetricSnapshot toDomain(DailyMetricSnapshotJpaEntity entity) {
        return DailyMetricSnapshot.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                entity.getSnapshotDate(),
                entity.getSentimentScore(),
                SentimentScoreLabel.valueOf(entity.getSentimentScoreLabel()),
                entity.getTotalMentions(),
                entity.getNegativeCount(),
                entity.getNeutralCount(),
                entity.getPositiveCount(),
                entity.getNegativePercent(),
                entity.getNeutralPercent(),
                entity.getPositivePercent(),
                entity.getVariationVsYesterday(),
                SourceType.valueOf(entity.getTopSource()),
                SentimentIndex.valueOf(entity.getTopSourceSentimentIndex()),
                entity.getCalculatedAt()
        );
    }
}
