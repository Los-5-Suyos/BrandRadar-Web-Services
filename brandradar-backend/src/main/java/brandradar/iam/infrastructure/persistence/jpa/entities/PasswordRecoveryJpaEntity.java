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
@Table(name = "PasswordRecovery")
public class PasswordRecoveryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PWR_id")
    private Long id;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @Column(name = "PWR_token", nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "PWR_status", nullable = false, length = 20)
    private String status;

    @Column(name = "PWR_expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "PWR_used_at")
    private Instant usedAt;

    @CreatedDate
    @Column(name = "PWR_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public PasswordRecoveryJpaEntity(Long id, Long userId, String token, String status,
                                     Instant expiresAt, Instant usedAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
    }
}