package brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ReputationReportSchedule", uniqueConstraints = @UniqueConstraint(columnNames = "BWS_id"))
public class ReputationReportScheduleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RPS_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "RPS_email", nullable = false, length = 255)
    private String email;

    @Column(name = "RPS_frequency", nullable = false, length = 20)
    private String frequency;

    @Column(name = "RPS_day_of_week", length = 10)
    private String dayOfWeek;

    @Column(name = "RPS_format", nullable = false, length = 10)
    private String format;

    @Column(name = "RPS_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "RPS_next_run_at")
    private Instant nextRunAt;

    @LastModifiedDate
    @Column(name = "RPS_updated_at", nullable = false)
    private Instant updatedAt;

    public ReputationReportScheduleJpaEntity(Long id, Long workspaceId, String email, String frequency,
                                             String dayOfWeek, String format, Boolean isActive, Instant nextRunAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.email = email;
        this.frequency = frequency;
        this.dayOfWeek = dayOfWeek;
        this.format = format;
        this.isActive = isActive != null ? isActive : true;
        this.nextRunAt = nextRunAt;
    }
}