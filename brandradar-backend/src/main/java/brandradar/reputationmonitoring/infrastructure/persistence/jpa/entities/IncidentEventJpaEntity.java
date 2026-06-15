package brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "IncidentEvent")
public class IncidentEventJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IEV_id")
    private Long id;

    @Column(name = "RIN_id", nullable = false)
    private Long incidentId;

    @Column(name = "IEV_event_type", nullable = false, length = 30)
    private String eventType;

    @Column(name = "IEV_status", nullable = false, length = 20)
    private String status;

    @Column(name = "IEV_performed_by")
    private Long performedBy;

    @CreatedDate
    @Column(name = "IEV_occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;

    public IncidentEventJpaEntity(Long id, Long incidentId, String eventType,
                                  String status, Long performedBy) {
        this.id = id;
        this.incidentId = incidentId;
        this.eventType = eventType;
        this.status = status;
        this.performedBy = performedBy;
    }
}