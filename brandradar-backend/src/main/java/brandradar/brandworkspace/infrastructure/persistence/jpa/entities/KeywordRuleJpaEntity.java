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
@Table(name = "KeywordRule")
public class KeywordRuleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KWR_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "KWR_keyword", nullable = false, length = 255)
    private String keyword;

    @Column(name = "KWR_match_type", nullable = false, length = 20)
    private String matchType;

    @Column(name = "KWR_weight", nullable = false)
    private Double weight;

    @Column(name = "KWR_is_active", nullable = false)
    private Boolean isActive;

    @CreatedDate
    @Column(name = "KWR_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public KeywordRuleJpaEntity(Long id, Long brandId, String keyword, String matchType,
                                Double weight, Boolean isActive) {
        this.id = id;
        this.brandId = brandId;
        this.keyword = keyword;
        this.matchType = matchType;
        this.weight = weight;
        this.isActive = isActive;
    }
}