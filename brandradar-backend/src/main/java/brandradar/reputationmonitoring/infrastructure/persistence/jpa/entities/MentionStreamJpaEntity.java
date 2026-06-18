package brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities;

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
@Table(name = "MentionStream")
public class MentionStreamJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MES_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "MES_period_from", nullable = false)
    private Instant periodFrom;

    @Column(name = "MES_period_to", nullable = false)
    private Instant periodTo;

    @Column(name = "MES_status", nullable = false, length = 20)
    private String status;

    @CreatedDate
    @Column(name = "MES_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "MES_updated_at", nullable = false)
    private Instant updatedAt;

    public MentionStreamJpaEntity(Long id, Long brandId, Instant periodFrom,
                                  Instant periodTo, String status) {
        this.id = id;
        this.brandId = brandId;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.status = status;
    }
}