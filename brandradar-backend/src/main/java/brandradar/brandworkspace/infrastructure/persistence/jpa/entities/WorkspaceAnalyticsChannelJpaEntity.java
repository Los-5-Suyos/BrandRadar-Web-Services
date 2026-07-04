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
@Table(name = "WorkspaceAnalyticsChannel")
public class WorkspaceAnalyticsChannelJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WAC2_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "WAC2_channel", nullable = false, length = 20)
    private String channel;

    @Column(name = "WAC2_is_active", nullable = false)
    private Boolean isActive;

    @CreatedDate
    @Column(name = "WAC2_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public WorkspaceAnalyticsChannelJpaEntity(Long id, Long workspaceId, String channel, Boolean isActive) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.channel = channel;
        this.isActive = isActive;
    }
}