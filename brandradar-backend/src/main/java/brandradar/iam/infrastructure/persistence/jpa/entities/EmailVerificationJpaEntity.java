package brandradar.iam.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "EmailVerification")
public class EmailVerificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMV_id")
    private Long id;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @Column(name = "EMV_token", nullable = false, unique = true)
    private String token;

    @Column(name = "EMV_status", nullable = false)
    private String status;

    @Column(name = "EMV_expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "EMV_used_at")
    private Instant usedAt;

    @Column(name = "EMV_created_at", updatable = false)
    private Instant createdAt;

    public EmailVerificationJpaEntity() {}

    public EmailVerificationJpaEntity(Long userId, String token, String status, Instant expiresAt, Instant usedAt, Instant createdAt) {
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public Instant getUsedAt() { return usedAt; }
    public void setUsedAt(Instant usedAt) { this.usedAt = usedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
