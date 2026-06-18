package brandradar.infrastructurehealth.application.commands;

public record CreateInfraIncidentCommand(
        Long serviceHealthCheckId,
        String incidentType,
        Integer severityLevel,
        String severityLabel
) {}