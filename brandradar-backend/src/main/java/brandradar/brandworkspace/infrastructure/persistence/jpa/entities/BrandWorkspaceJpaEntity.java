package brandradar.brandworkspace.infrastructure.persistence.jpa.entities;

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
@Table(name = "BrandWorkspace")
public class BrandWorkspaceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BWS_id")
    private Long id;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @Column(name = "BWS_name", nullable = false, length = 255)
    private String name;

    @Column(name = "BWS_plan", nullable = false, length = 20)
    private String plan;

    @Column(name = "BWS_status", nullable = false, length = 30)
    private String status;

    @Column(name = "BWS_policy_max_brands", nullable = false)
    private Integer policyMaxBrands;

    @Column(name = "BWS_policy_alert_quota", nullable = false)
    private Integer policyAlertQuota;

    @CreatedDate
    @Column(name = "BWS_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "BWS_updated_at", nullable = false)
    private Instant updatedAt;

    public BrandWorkspaceJpaEntity(Long id, Long userId, String name, String plan,
                                   String status, Integer policyMaxBrands, Integer policyAlertQuota) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.plan = plan;
        this.status = status;
        this.policyMaxBrands = policyMaxBrands;
        this.policyAlertQuota = policyAlertQuota;
    }
}