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
@Table(name = "BrandWorkspace")
public class BrandWorkspaceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BWS_id")
    private Long id;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @Column(name = "BWS_name", nullable = false, length = 255)
    private String name;

    @Column(name = "BWS_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "BWS_status", nullable = false, length = 20)
    private String status;

    @CreatedDate
    @Column(name = "BWS_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "BWS_updated_at", nullable = false)
    private Instant updatedAt;

    public BrandWorkspaceJpaEntity(Long id, Long userId, String name, String description, String status,
                                   Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }
}
