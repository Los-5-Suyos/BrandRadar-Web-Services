package brandradar.brandworkspace.infrastructure.persistence.jpa.entities;

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
@Table(name = "Subscription", uniqueConstraints = @UniqueConstraint(columnNames = "BWS_id"))
public class SubscriptionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUB_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "SUB_plan", nullable = false, length = 20)
    private String plan;

    @Column(name = "SUB_billing_period", nullable = false, length = 10)
    private String billingPeriod;

    @Column(name = "SUB_status", nullable = false, length = 20)
    private String status;

    @Column(name = "SUB_fake_card_last4", length = 4)
    private String fakeCardLast4;

    @Column(name = "SUB_fake_card_brand", length = 20)
    private String fakeCardBrand;

    @Column(name = "SUB_started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "SUB_renews_at")
    private Instant renewsAt;

    @CreatedDate
    @Column(name = "SUB_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public SubscriptionJpaEntity(Long id, Long workspaceId, String plan, String billingPeriod, String status,
                                 String fakeCardLast4, String fakeCardBrand, Instant startedAt, Instant renewsAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.plan = plan;
        this.billingPeriod = billingPeriod;
        this.status = status;
        this.fakeCardLast4 = fakeCardLast4;
        this.fakeCardBrand = fakeCardBrand;
        this.startedAt = startedAt;
        this.renewsAt = renewsAt;
    }
}