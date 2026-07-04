package brandradar.sentimentintelligence.infrastructure.persistence.jpa.mappers;

import brandradar.sentimentintelligence.domain.model.aggregates.ChannelInsight;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities.ChannelInsightJpaEntity;

public class ChannelInsightPersistenceMapper {

    private ChannelInsightPersistenceMapper() {}

    public static ChannelInsightJpaEntity toJpaEntity(ChannelInsight insight) {
        return new ChannelInsightJpaEntity(
                insight.getId(),
                insight.getBrandId(),
                insight.getChannelType(),
                insight.getInsightText()
        );
    }

    public static ChannelInsight toDomain(ChannelInsightJpaEntity entity) {
        return ChannelInsight.rehydrate(
                entity.getId(),
                entity.getBrandId(),
                entity.getChannelType(),
                entity.getInsightText()
        );
    }
}