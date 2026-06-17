package brandradar.infrastructurehealth.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class InfraIncident {

    private final Long id;
    private final Long serviceHealthCheckId;
    private final String incidentType;
    private final Integer severityLevel;
    private final String severityLabel;
    private final String status;
    private final BigDecimal estimatedReputationalImpact;
    private final String resolutionSummary;
    private final String resolutionRootCause;
    private final String resolutionPreventive;
    private final Instant detectedAt;
    private final Instant resolvedAt;

    private InfraIncident(Long id, Long serviceHealthCheckId, String incidentType, Integer severityLevel, String severityLabel, String status, BigDecimal estimatedReputationalImpact, String resolutionSummary, String resolutionRootCause, String resolutionPreventive, Instant detectedAt, Instant resolvedAt) {
        this.id = id;
        this.serviceHealthCheckId = Objects.requireNonNull(serviceHealthCheckId, "ServiceHealthCheckId is required");
        this.incidentType = Objects.requireNonNull(incidentType, "IncidentType is required");
        this.severityLevel = severityLevel != null ? severityLevel : 1;
        this.severityLabel = severityLabel;
        this.status = status != null ? status : "OPEN";
        this.estimatedReputationalImpact = estimatedReputationalImpact != null ? estimatedReputationalImpact : BigDecimal.ZERO;
        this.resolutionSummary = resolutionSummary;
        this.resolutionRootCause = resolutionRootCause;
        this.resolutionPreventive = resolutionPreventive;
        this.detectedAt = detectedAt;
        this.resolvedAt = resolvedAt;
    }

    public static InfraIncident create(Long serviceHealthCheckId, String incidentType, Integer severityLevel, String severityLabel) {
        return new InfraIncident(null, serviceHealthCheckId, incidentType, severityLevel,
                severityLabel, "OPEN", BigDecimal.ZERO, null, null, null, Instant.now(), null);
    }

    public static InfraIncident rehydrate(Long id, Long serviceHealthCheckId, String incidentType, Integer severityLevel, String severityLabel, String status, BigDecimal estimatedReputationalImpact, String resolutionSummary, String resolutionRootCause, String resolutionPreventive, Instant detectedAt, Instant resolvedAt) {
        return new InfraIncident(id, serviceHealthCheckId, incidentType, severityLevel, severityLabel, status, estimatedReputationalImpact, resolutionSummary, resolutionRootCause, resolutionPreventive, detectedAt, resolvedAt);
    }

    public Long getId() { return id; }
    public Long getServiceHealthCheckId() { return serviceHealthCheckId; }
    public String getIncidentType() { return incidentType; }
    public Integer getSeverityLevel() { return severityLevel; }
    public String getSeverityLabel() { return severityLabel; }
    public String getStatus() { return status; }
    public BigDecimal getEstimatedReputationalImpact() { return estimatedReputationalImpact; }
    public String getResolutionSummary() { return resolutionSummary; }
    public String getResolutionRootCause() { return resolutionRootCause; }
    public String getResolutionPreventive() { return resolutionPreventive; }
    public Instant getDetectedAt() { return detectedAt; }
    public Instant getResolvedAt() { return resolvedAt; }
}