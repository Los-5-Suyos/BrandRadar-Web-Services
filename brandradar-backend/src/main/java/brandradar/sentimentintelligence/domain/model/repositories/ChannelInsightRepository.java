package brandradar.sentimentintelligence.domain.model.repositories;

import brandradar.sentimentintelligence.domain.model.aggregates.ChannelInsight;

import java.util.List;
import java.util.Optional;

public interface ChannelInsightRepository {
    ChannelInsight save(ChannelInsight insight);
    Optional<ChannelInsight> findByBrandIdAndChannelType(Long brandId, String channelType);
    List<ChannelInsight> findByBrandId(Long brandId);
}