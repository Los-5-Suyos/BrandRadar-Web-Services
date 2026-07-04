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
@Table(name = "AuthSession")
public class AuthSessionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUS_id")
    private Long id;

    @Column(name = "USU_id", nullable = false)
    private Long userId;

    @Column(name = "AUS_token_jwt", nullable = false, columnDefinition = "TEXT")
    private String tokenJwt;

    @Column(name = "AUS_refresh_token", nullable = false, length = 512)
    private String refreshToken;

    @Column(name = "AUS_ip_address", length = 45)
    private String ipAddress;

    @Column(name = "AUS_expires_at", nullable = false)
    private Instant expiresAt;

    @CreatedDate
    @Column(name = "AUS_issued_at", nullable = false, updatable = false)
    private Instant issuedAt;

    @Column(name = "AUS_invalidated_at")
    private Instant invalidatedAt;
}