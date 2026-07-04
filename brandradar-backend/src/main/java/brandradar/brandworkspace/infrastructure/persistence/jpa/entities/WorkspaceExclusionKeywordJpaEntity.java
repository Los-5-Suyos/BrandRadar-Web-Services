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
@Table(name = "WorkspaceExclusionKeyword")
public class WorkspaceExclusionKeywordJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WEK_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "WEK_keyword", nullable = false, length = 255)
    private String keyword;

    @CreatedDate
    @Column(name = "WEK_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public WorkspaceExclusionKeywordJpaEntity(Long id, Long workspaceId, String keyword) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.keyword = keyword;
    }
}