package brandradar.infrastructurehealth.interfaces.rest.resources;

import java.time.Instant;

public record ServiceHealthCheckResource(
        Long id,
        String serviceName,
        String endpointUrl,
        String endpointMethod,
        Integer endpointTimeoutMs,
        String status,
        Integer uptimeTotalChecks,
        Integer uptimeSuccessfulChecks,
        Integer uptimeWindowDays,
        Instant lastCheckedAt,
        Instant createdAt,
        Instant updatedAt
) {}