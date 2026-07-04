package brandradar.reputationmonitoring.interfaces.rest.resources;

public record UpdateIncidentStatusResource(
        String status,
        Integer progressPct,
        String resolutionNotes
) {}