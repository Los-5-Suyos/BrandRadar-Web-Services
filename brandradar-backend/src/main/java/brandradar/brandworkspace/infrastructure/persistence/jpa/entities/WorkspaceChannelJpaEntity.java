package brandradar.brandworkspace.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "WorkspaceAllowedChannel")
public class WorkspaceChannelJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WAC_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "WAC_channel_type", nullable = false, length = 20)
    private String channelType;

    public WorkspaceChannelJpaEntity(Long id, Long workspaceId, String channelType) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.channelType = channelType;
    }
}