package brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ReputationIncident")
public class ReputationIncidentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RIN_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "MES_id")
    private Long mentionStreamId;

    @Column(name = "CRA_id")
    private Long crisisAlertId;

    @Column(name = "RIN_severity_level", nullable = false)
    private Integer severityLevel;

    @Column(name = "RIN_severity_label", length = 50)
    private String severityLabel;

    @Column(name = "RIN_title", length = 255)
    private String title;

    @Column(name = "RIN_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "RIN_status", nullable = false, length = 20)
    private String status;

    @Column(name = "RIN_progress_pct")
    private Integer progressPct;

    @Column(name = "RIN_assigned_to")
    private Long assignedTo;

    @Column(name = "RIN_impact_score", nullable = false)
    private BigDecimal impactScore;

    @Column(name = "RIN_resolution_summary", columnDefinition = "TEXT")
    private String resolutionSummary;

    @Column(name = "RIN_resolution_actions", columnDefinition = "TEXT")
    private String resolutionActions;

    @Column(name = "RIN_resolved_by")
    private Long resolvedBy;

    @Column(name = "RIN_resolved_at")
    private Instant resolvedAt;

    @CreatedDate
    @Column(name = "RIN_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "RIN_updated_at", nullable = false)
    private Instant updatedAt;

    public ReputationIncidentJpaEntity(Long id, Long brandId, Long mentionStreamId, Long crisisAlertId,
                                       Integer severityLevel, String severityLabel, String title, String description,
                                       String status, Integer progressPct, Long assignedTo, BigDecimal impactScore,
                                       String resolutionSummary, String resolutionActions, Long resolvedBy,
                                       Instant resolvedAt) {
        this.id = id;
        this.brandId = brandId;
        this.mentionStreamId = mentionStreamId;
        this.crisisAlertId = crisisAlertId;
        this.severityLevel = severityLevel;
        this.severityLabel = severityLabel;
        this.title = title;
        this.description = description;
        this.status = status;
        this.progressPct = progressPct;
        this.assignedTo = assignedTo;
        this.impactScore = impactScore;
        this.resolutionSummary = resolutionSummary;
        this.resolutionActions = resolutionActions;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = resolvedAt;
    }
}