package brandradar.infrastructurehealth.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "InfraIncident")
public class InfraIncidentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INI_id")
    private Long id;

    @Column(name = "SHC_id", nullable = false)
    private Long serviceHealthCheckId;

    @Column(name = "INI_incident_type", nullable = false, length = 20)
    private String incidentType;

    @Column(name = "INI_severity_level", nullable = false)
    private Integer severityLevel;

    @Column(name = "INI_severity_label", length = 50)
    private String severityLabel;

    @Column(name = "INI_status", nullable = false, length = 20)
    private String status;

    @Column(name = "INI_estimated_reputational_impact", nullable = false)
    private BigDecimal estimatedReputationalImpact;

    @Column(name = "INI_resolution_summary", columnDefinition = "TEXT")
    private String resolutionSummary;

    @Column(name = "INI_resolution_root_cause", columnDefinition = "TEXT")
    private String resolutionRootCause;

    @Column(name = "INI_resolution_preventive", columnDefinition = "TEXT")
    private String resolutionPreventive;

    @Column(name = "INI_detected_at")
    private Instant detectedAt;

    @Column(name = "INI_resolved_at")
    private Instant resolvedAt;

    public InfraIncidentJpaEntity(Long id, Long serviceHealthCheckId, String incidentType, Integer severityLevel, String severityLabel, String status, BigDecimal estimatedReputationalImpact, String resolutionSummary, String resolutionRootCause, String resolutionPreventive, Instant detectedAt, Instant resolvedAt) {
        this.id = id;
        this.serviceHealthCheckId = serviceHealthCheckId;
        this.incidentType = incidentType;
        this.severityLevel = severityLevel;
        this.severityLabel = severityLabel;
        this.status = status;
        this.estimatedReputationalImpact = estimatedReputationalImpact;
        this.resolutionSummary = resolutionSummary;
        this.resolutionRootCause = resolutionRootCause;
        this.resolutionPreventive = resolutionPreventive;
        this.detectedAt = detectedAt;
        this.resolvedAt = resolvedAt;
    }
}