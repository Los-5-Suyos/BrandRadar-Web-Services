package brandradar.reputationmonitoring.interfaces.rest.resources;

public record ChannelCountResource(
        String platform,
        Long count
) {}