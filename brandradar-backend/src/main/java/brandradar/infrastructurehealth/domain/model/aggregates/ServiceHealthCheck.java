package brandradar.infrastructurehealth.domain.model.aggregates;

import java.time.Instant;
import java.util.Objects;

public class ServiceHealthCheck {

    private final Long id;
    private final String serviceName;
    private final String endpointUrl;
    private final String endpointMethod;
    private final Integer endpointTimeoutMs;
    private final String status;
    private final Integer uptimeTotalChecks;
    private final Integer uptimeSuccessfulChecks;
    private final Integer uptimeWindowDays;
    private final Instant lastCheckedAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    private ServiceHealthCheck(Long id, String serviceName, String endpointUrl, String endpointMethod, Integer endpointTimeoutMs, String status, Integer uptimeTotalChecks, Integer uptimeSuccessfulChecks, Integer uptimeWindowDays, Instant lastCheckedAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.serviceName = Objects.requireNonNull(serviceName, "ServiceName is required");
        this.endpointUrl = Objects.requireNonNull(endpointUrl, "EndpointUrl is required");
        this.endpointMethod = endpointMethod != null ? endpointMethod : "GET";
        this.endpointTimeoutMs = endpointTimeoutMs != null ? endpointTimeoutMs : 5000;
        this.status = status != null ? status : "HEALTHY";
        this.uptimeTotalChecks = uptimeTotalChecks != null ? uptimeTotalChecks : 0;
        this.uptimeSuccessfulChecks = uptimeSuccessfulChecks != null ? uptimeSuccessfulChecks : 0;
        this.uptimeWindowDays = uptimeWindowDays != null ? uptimeWindowDays : 30;
        this.lastCheckedAt = lastCheckedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ServiceHealthCheck create(String serviceName, String endpointUrl, String endpointMethod, Integer endpointTimeoutMs) {
        return new ServiceHealthCheck(null, serviceName, endpointUrl, endpointMethod, endpointTimeoutMs, "HEALTHY", 0, 0, 30, null, null, null);
    }

    public static ServiceHealthCheck rehydrate(Long id, String serviceName, String endpointUrl, String endpointMethod, Integer endpointTimeoutMs, String status, Integer uptimeTotalChecks, Integer uptimeSuccessfulChecks, Integer uptimeWindowDays, Instant lastCheckedAt, Instant createdAt, Instant updatedAt) {
        return new ServiceHealthCheck(id, serviceName, endpointUrl, endpointMethod, endpointTimeoutMs, status, uptimeTotalChecks, uptimeSuccessfulChecks, uptimeWindowDays, lastCheckedAt, createdAt, updatedAt);
    }

    public Long getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getEndpointUrl() { return endpointUrl; }
    public String getEndpointMethod() { return endpointMethod; }
    public Integer getEndpointTimeoutMs() { return endpointTimeoutMs; }
    public String getStatus() { return status; }
    public Integer getUptimeTotalChecks() { return uptimeTotalChecks; }
    public Integer getUptimeSuccessfulChecks() { return uptimeSuccessfulChecks; }
    public Integer getUptimeWindowDays() { return uptimeWindowDays; }
    public Instant getLastCheckedAt() { return lastCheckedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}