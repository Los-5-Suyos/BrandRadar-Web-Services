package brandradar.brandworkspace.infrastructure.persistence.jpa.entities;

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
@Table(name = "MonitoringChannel")
public class MonitoringChannelJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOC_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "MOC_channel_type", nullable = false, length = 20)
    private String channelType;

    @Column(name = "MOC_status", nullable = false, length = 20)
    private String status;

    @Column(name = "MOC_credentials_enc", columnDefinition = "TEXT")
    private String credentialsEnc;

    @Column(name = "MOC_is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "MOC_last_sync_at")
    private Instant lastSyncAt;

    @CreatedDate
    @Column(name = "MOC_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "MOC_updated_at", nullable = false)
    private Instant updatedAt;
}