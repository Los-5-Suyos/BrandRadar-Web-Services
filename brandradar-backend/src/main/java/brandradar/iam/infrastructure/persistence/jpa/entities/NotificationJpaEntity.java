package brandradar.iam.infrastructure.persistence.jpa.entities;

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
@Table(name = "Notification")
public class NotificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOT_id")
    private Long id;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @Column(name = "BRA_id")
    private Long brandId;

    @Column(name = "NOT_type", nullable = false, length = 30)
    private String type;

    @Column(name = "NOT_title", nullable = false, length = 255)
    private String title;

    @Column(name = "NOT_message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "NOT_is_read", nullable = false)
    private Boolean isRead;

    @CreatedDate
    @Column(name = "NOT_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public NotificationJpaEntity(Long id, Long userId, Long brandId, String type, String title,
                                 String message, Boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.brandId = brandId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
    }
}