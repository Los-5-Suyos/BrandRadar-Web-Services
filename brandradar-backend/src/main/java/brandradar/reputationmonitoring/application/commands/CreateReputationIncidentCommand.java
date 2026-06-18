package brandradar.reputationmonitoring.application.commands;

public record CreateReputationIncidentCommand(
        Long brandId,
        Long mentionStreamId,
        Integer severityLevel,
        String severityLabel
) {}