package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.time.Instant;

public record CrisisAnalysisLogResource(
        Long id,
        String pattern,
        String keywords,
        String geofocus,
        String diagnostico,
        String accion,
        Instant createdAt
) {}