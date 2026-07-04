package brandradar.reputationmonitoring.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class ReputationIncident {

    private final Long id;
    private final Long brandId;
    private final Long mentionStreamId;
    private final Long crisisAlertId;
    private final Integer severityLevel;
    private final String severityLabel;
    private final String title;
    private final String description;
    private final String status;
    private final Integer progressPct;
    private final Long assignedTo;
    private final BigDecimal impactScore;
    private final String resolutionSummary;
    private final String resolutionActions;
    private final Long resolvedBy;
    private final Instant resolvedAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    private ReputationIncident(Long id, Long brandId, Long mentionStreamId, Long crisisAlertId,
                               Integer severityLevel, String severityLabel, String title, String description,
                               String status, Integer progressPct, Long assignedTo, BigDecimal impactScore,
                               String resolutionSummary, String resolutionActions, Long resolvedBy,
                               Instant resolvedAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "BrandId is required");
        this.mentionStreamId = mentionStreamId;
        this.crisisAlertId = crisisAlertId;
        this.severityLevel = severityLevel != null ? severityLevel : 1;
        this.severityLabel = severityLabel;
        this.title = title;
        this.description = description;
        this.status = status != null ? status : "ACTIVO";
        this.progressPct = progressPct != null ? progressPct : 0;
        this.assignedTo = assignedTo;
        this.impactScore = impactScore != null ? impactScore : BigDecimal.ZERO;
        this.resolutionSummary = resolutionSummary;
        this.resolutionActions = resolutionActions;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = resolvedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ReputationIncident create(Long brandId, Long mentionStreamId, Long crisisAlertId,
                                            Integer severityLevel, String severityLabel,
                                            String title, String description) {
        return new ReputationIncident(null, brandId, mentionStreamId, crisisAlertId, severityLevel,
                severityLabel, title, description, "ACTIVO", 0, null, BigDecimal.ZERO,
                null, null, null, null, null, null);
    }

    public static ReputationIncident rehydrate(Long id, Long brandId, Long mentionStreamId, Long crisisAlertId,
                                               Integer severityLevel, String severityLabel, String title,
                                               String description, String status, Integer progressPct,
                                               Long assignedTo, BigDecimal impactScore, String resolutionSummary,
                                               String resolutionActions, Long resolvedBy, Instant resolvedAt,
                                               Instant createdAt, Instant updatedAt) {
        return new ReputationIncident(id, brandId, mentionStreamId, crisisAlertId, severityLevel,
                severityLabel, title, description, status, progressPct, assignedTo, impactScore,
                resolutionSummary, resolutionActions, resolvedBy, resolvedAt, createdAt, updatedAt);
    }

    /** ACTIVO → MONITOREADO → RESUELTO, con progreso opcional. */
    public ReputationIncident withStatusUpdate(String newStatus, Integer newProgressPct, String resolutionNotes) {
        boolean resolving = "RESUELTO".equals(newStatus);
        return new ReputationIncident(this.id, this.brandId, this.mentionStreamId, this.crisisAlertId,
                this.severityLevel, this.severityLabel, this.title, this.description,
                newStatus != null ? newStatus : this.status,
                newProgressPct != null ? newProgressPct : (resolving ? 100 : this.progressPct),
                this.assignedTo, this.impactScore,
                resolutionNotes != null ? resolutionNotes : this.resolutionSummary,
                this.resolutionActions, this.resolvedBy,
                resolving ? Instant.now() : this.resolvedAt,
                this.createdAt, this.updatedAt);
    }

    public ReputationIncident withAssignedTo(Long userId) {
        return new ReputationIncident(this.id, this.brandId, this.mentionStreamId, this.crisisAlertId,
                this.severityLevel, this.severityLabel, this.title, this.description, this.status,
                this.progressPct, userId, this.impactScore, this.resolutionSummary,
                this.resolutionActions, this.resolvedBy, this.resolvedAt, this.createdAt, this.updatedAt);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public Long getMentionStreamId() { return mentionStreamId; }
    public Long getCrisisAlertId() { return crisisAlertId; }
    public Integer getSeverityLevel() { return severityLevel; }
    public String getSeverityLabel() { return severityLabel; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Integer getProgressPct() { return progressPct; }
    public Long getAssignedTo() { return assignedTo; }
    public BigDecimal getImpactScore() { return impactScore; }
    public String getResolutionSummary() { return resolutionSummary; }
    public String getResolutionActions() { return resolutionActions; }
    public Long getResolvedBy() { return resolvedBy; }
    public Instant getResolvedAt() { return resolvedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}