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
@Table(name = "CrisisAlertAcknowledgement")
public class CrisisAlertAcknowledgementJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAA_id")
    private Long id;

    @Column(name = "CRA_id", nullable = false)
    private Long alertId;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @CreatedDate
    @Column(name = "CAA_acknowledged_at", nullable = false, updatable = false)
    private Instant acknowledgedAt;
}