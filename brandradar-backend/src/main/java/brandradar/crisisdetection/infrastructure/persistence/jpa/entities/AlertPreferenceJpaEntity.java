package brandradar.crisisdetection.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "AlertPreference", uniqueConstraints = @UniqueConstraint(columnNames = {"BRA_id", "ALP_key"}))
public class AlertPreferenceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALP_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "ALP_key", nullable = false, length = 30)
    private String key;

    @Column(name = "ALP_enabled", nullable = false)
    private Boolean enabled;

    public AlertPreferenceJpaEntity(Long id, Long brandId, String key, Boolean enabled) {
        this.id = id;
        this.brandId = brandId;
        this.key = key;
        this.enabled = enabled;
    }
}