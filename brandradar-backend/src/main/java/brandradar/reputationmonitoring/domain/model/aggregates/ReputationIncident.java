package brandradar.reputationmonitoring.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class ReputationIncident {

    private final Long id;
    private final Long brandId;
    private final Long mentionStreamId;
    private final Integer severityLevel;
    private final String severityLabel;
    private final String status;
    private final Long assignedTo;
    private final BigDecimal impactScore;
    private final String resolutionSummary;
    private final String resolutionActions;
    private final Long resolvedBy;
    private final Instant resolvedAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    private ReputationIncident(Long id, Long brandId, Long mentionStreamId, Integer severityLevel,
                               String severityLabel, String status, Long assignedTo, BigDecimal impactScore,
                               String resolutionSummary, String resolutionActions, Long resolvedBy,
                               Instant resolvedAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "BrandId is required");
        this.mentionStreamId = mentionStreamId;
        this.severityLevel = severityLevel != null ? severityLevel : 1;
        this.severityLabel = severityLabel;
        this.status = status != null ? status : "UNASSIGNED";
        this.assignedTo = assignedTo;
        this.impactScore = impactScore != null ? impactScore : BigDecimal.ZERO;
        this.resolutionSummary = resolutionSummary;
        this.resolutionActions = resolutionActions;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = resolvedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ReputationIncident create(Long brandId, Long mentionStreamId, Integer severityLevel, String severityLabel) {
        return new ReputationIncident(null, brandId, mentionStreamId, severityLevel, severityLabel,
                "UNASSIGNED", null, BigDecimal.ZERO, null, null, null, null, null, null);
    }

    public static ReputationIncident rehydrate(Long id, Long brandId, Long mentionStreamId,
                                               Integer severityLevel, String severityLabel, String status,
                                               Long assignedTo, BigDecimal impactScore, String resolutionSummary,
                                               String resolutionActions, Long resolvedBy, Instant resolvedAt,
                                               Instant createdAt, Instant updatedAt) {
        return new ReputationIncident(id, brandId, mentionStreamId, severityLevel, severityLabel,
                status, assignedTo, impactScore, resolutionSummary, resolutionActions,
                resolvedBy, resolvedAt, createdAt, updatedAt);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public Long getMentionStreamId() { return mentionStreamId; }
    public Integer getSeverityLevel() { return severityLevel; }
    public String getSeverityLabel() { return severityLabel; }
    public String getStatus() { return status; }
    public Long getAssignedTo() { return assignedTo; }
    public BigDecimal getImpactScore() { return impactScore; }
    public String getResolutionSummary() { return resolutionSummary; }
    public String getResolutionActions() { return resolutionActions; }
    public Long getResolvedBy() { return resolvedBy; }
    public Instant getResolvedAt() { return resolvedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}