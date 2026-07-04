package brandradar.shared.interfaces.rest.resources;

import java.util.List;

public record DashboardChannelsResource(
        Long brandId,
        List<ChannelScore> channels
) {
    public record ChannelScore(
            String channelType,
            Double sentimentIndex,
            Long mentionsCount,
            String topInsight
    ) {}
}