package brandradar.crisisdetection.infrastructure.persistence.jpa.entities;

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
@Table(name = "CrisisAlertNotifiedStakeholder")
public class CrisisAlertNotifiedStakeholderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CNS_id")
    private Long id;

    @Column(name = "CRA_id", nullable = false)
    private Long alertId;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @CreatedDate
    @Column(name = "CNS_notified_at", nullable = false, updatable = false)
    private Instant notifiedAt;
}