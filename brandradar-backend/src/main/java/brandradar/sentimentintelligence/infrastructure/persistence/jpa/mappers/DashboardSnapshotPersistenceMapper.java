package brandradar.sentimentintelligence.infrastructure.persistence.jpa.mappers;

import brandradar.sentimentintelligence.domain.model.aggregates.DashboardSnapshot;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities.DashboardSnapshotJpaEntity;

public class DashboardSnapshotPersistenceMapper {

    private DashboardSnapshotPersistenceMapper() {}

    public static DashboardSnapshotJpaEntity toJpaEntity(DashboardSnapshot snapshot) {
        return new DashboardSnapshotJpaEntity(
                snapshot.getId(),
                snapshot.getBrandId(),
                snapshot.getDate(),
                snapshot.getSentimentScore(),
                snapshot.getMentionsCount(),
                snapshot.getPositivePct(),
                snapshot.getNeutralPct(),
                snapshot.getNegativePct(),
                snapshot.getCrisisAnalysisText()
        );
    }

    public static DashboardSnapshot toDomain(DashboardSnapshotJpaEntity entity) {
        return DashboardSnapshot.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getDate(),
                entity.getSentimentScore(),
                entity.getMentionsCount(),
                entity.getPositivePct(),
                entity.getNeutralPct(),
                entity.getNegativePct(),
                entity.getCrisisAnalysisText()
        );
    }
}