package brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "ReputationReportIncident")
public class ReputationReportIncidentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RRI_id")
    private Long id;

    @Column(name = "RPR_id", nullable = false)
    private Long reportId;

    @Column(name = "RIN_id", nullable = false)
    private Long incidentId;
}