package brandradar.infrastructurehealth.infrastructure.persistence.jpa.entities;

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
@Table(name = "HealthCheckResult")
public class HealthCheckResultJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HCR_id")
    private Long id;

    @Column(name = "SHC_id", nullable = false)
    private Long serviceHealthCheckId;

    @Column(name = "HCR_response_time_ms", nullable = false)
    private Long responseTimeMs;

    @Column(name = "HCR_http_status")
    private Short httpStatus;

    @Column(name = "HCR_is_healthy", nullable = false)
    private Boolean isHealthy;

    @CreatedDate
    @Column(name = "HCR_checked_at", nullable = false, updatable = false)
    private Instant checkedAt;
}