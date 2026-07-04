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
@Table(name = "CrisisAnalysisLog")
public class CrisisAnalysisLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAL_id")
    private Long id;

    @Column(name = "RIN_id", nullable = false)
    private Long incidentId;

    @Column(name = "CAL_pattern", length = 255)
    private String pattern;

    @Column(name = "CAL_keywords", length = 500)
    private String keywords;

    @Column(name = "CAL_geofocus", length = 255)
    private String geofocus;

    @Column(name = "CAL_diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "CAL_accion", columnDefinition = "TEXT")
    private String accion;

    @CreatedDate
    @Column(name = "CAL_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public CrisisAnalysisLogJpaEntity(Long id, Long incidentId, String pattern, String keywords,
                                      String geofocus, String diagnostico, String accion) {
        this.id = id;
        this.incidentId = incidentId;
        this.pattern = pattern;
        this.keywords = keywords;
        this.geofocus = geofocus;
        this.diagnostico = diagnostico;
        this.accion = accion;
    }
}