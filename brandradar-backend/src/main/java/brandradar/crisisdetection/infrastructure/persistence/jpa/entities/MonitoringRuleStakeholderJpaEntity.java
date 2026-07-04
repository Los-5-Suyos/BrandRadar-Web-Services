package brandradar.crisisdetection.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "MonitoringRuleStakeholder")
public class MonitoringRuleStakeholderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MRS_id")
    private Long id;

    @Column(name = "MOR_id", nullable = false)
    private Long ruleId;

    @Column(name = "USU_id", nullable = false)
    private Long userId;
}