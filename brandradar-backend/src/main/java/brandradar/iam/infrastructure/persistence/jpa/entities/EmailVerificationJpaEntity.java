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
@Table(name = "EmailVerification")
public class EmailVerificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMV_id")
    private Long id;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @Column(name = "EMV_token", nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "EMV_status", nullable = false, length = 20)
    private String status;

    @Column(name = "EMV_expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "EMV_used_at")
    private Instant usedAt;

    @CreatedDate
    @Column(name = "EMV_created_at", nullable = false, updatable = false)
    private Instant createdAt;
}