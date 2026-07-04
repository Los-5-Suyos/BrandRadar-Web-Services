package brandradar.crisisdetection.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "MonitoringRuleChannel")
public class MonitoringRuleChannelJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MRC_id")
    private Long id;

    @Column(name = "MOR_id", nullable = false)
    private Long ruleId;

    @Column(name = "MRC_channel_type", nullable = false, length = 20)
    private String channelType;

    @Column(name = "MRC_is_active", nullable = false)
    private Boolean isActive;
}