package brandradar.reputationmonitoring.domain.model.aggregates;

import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.reputationmonitoring.domain.model.valueobjects.SeverityLevel;
import brandradar.reputationmonitoring.domain.model.valueobjects.IncidentStatus;
import brandradar.shared.infrastructure.persistence.jpa.audit.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "incidents")
public class Incident extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_workspace_id", nullable = false)
    private BrandWorkspace brandWorkspace;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeverityLevel severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IncidentStatus status;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "mention_count")
    private Integer mentionCount = 0;

    @Column(name = "resolution_percent")
    private Integer resolutionPercent = 0;

    @Column(name = "requires_immediate_action")
    private Boolean requiresImmediateAction = false;

    @Column(name = "pattern_keyword")
    private String patternKeyword;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;
}