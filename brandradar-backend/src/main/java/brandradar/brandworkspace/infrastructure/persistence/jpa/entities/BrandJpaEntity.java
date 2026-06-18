package brandradar.brandworkspace.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Brand")
public class BrandJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRA_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "BRA_name", nullable = false, length = 255)
    private String name;

    @Column(name = "BRA_reputation_score", nullable = false)
    private BigDecimal reputationScore;

    @Column(name = "BRA_reputation_calculated_at")
    private Instant reputationCalculatedAt;

    @CreatedDate
    @Column(name = "BRA_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "BRA_updated_at", nullable = false)
    private Instant updatedAt;

    public BrandJpaEntity(Long id, Long workspaceId, String name,
                          BigDecimal reputationScore, Instant reputationCalculatedAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.name = name;
        this.reputationScore = reputationScore;
        this.reputationCalculatedAt = reputationCalculatedAt;
    }
}