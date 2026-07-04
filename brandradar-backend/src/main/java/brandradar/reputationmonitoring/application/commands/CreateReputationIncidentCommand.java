package brandradar.reputationmonitoring.application.commands;

public record CreateReputationIncidentCommand(
        Long brandId,
        Long mentionStreamId,
        Long crisisAlertId,
        Integer severityLevel,
        String severityLabel,
        String title,
        String description
) {}