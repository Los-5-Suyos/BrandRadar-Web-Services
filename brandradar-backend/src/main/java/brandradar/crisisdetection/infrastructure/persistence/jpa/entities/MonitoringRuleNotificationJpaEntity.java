package brandradar.crisisdetection.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "MonitoringRuleNotification")
public class MonitoringRuleNotificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MRN_id")
    private Long id;

    @Column(name = "MOR_id", nullable = false)
    private Long ruleId;

    @Column(name = "MRN_notification_channel", nullable = false, length = 20)
    private String notificationChannel;
}