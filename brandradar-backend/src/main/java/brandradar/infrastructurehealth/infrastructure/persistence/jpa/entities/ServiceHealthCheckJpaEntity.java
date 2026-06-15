package brandradar.infrastructurehealth.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ServiceHealthCheck")
public class ServiceHealthCheckJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHC_id")
    private Long id;

    @Column(name = "SHC_service_name", nullable = false, length = 255)
    private String serviceName;

    @Column(name = "SHC_endpoint_url", nullable = false, length = 2000)
    private String endpointUrl;

    @Column(name = "SHC_endpoint_method", nullable = false, length = 10)
    private String endpointMethod;

    @Column(name = "SHC_endpoint_timeout_ms", nullable = false)
    private Integer endpointTimeoutMs;

    @Column(name = "SHC_status", nullable = false, length = 20)
    private String status;

    @Column(name = "SHC_uptime_total_checks", nullable = false)
    private Integer uptimeTotalChecks;

    @Column(name = "SHC_uptime_successful_checks", nullable = false)
    private Integer uptimeSuccessfulChecks;

    @Column(name = "SHC_uptime_window_days", nullable = false)
    private Integer uptimeWindowDays;

    @Column(name = "SHC_last_checked_at")
    private Instant lastCheckedAt;

    @CreatedDate
    @Column(name = "SHC_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "SHC_updated_at", nullable = false)
    private Instant updatedAt;

    public ServiceHealthCheckJpaEntity(Long id, String serviceName, String endpointUrl, String endpointMethod, Integer endpointTimeoutMs, String status, Integer uptimeTotalChecks, Integer uptimeSuccessfulChecks, Integer uptimeWindowDays, Instant lastCheckedAt) {
        this.id = id;
        this.serviceName = serviceName;
        this.endpointUrl = endpointUrl;
        this.endpointMethod = endpointMethod;
        this.endpointTimeoutMs = endpointTimeoutMs;
        this.status = status;
        this.uptimeTotalChecks = uptimeTotalChecks;
        this.uptimeSuccessfulChecks = uptimeSuccessfulChecks;
        this.uptimeWindowDays = uptimeWindowDays;
        this.lastCheckedAt = lastCheckedAt;
    }
}