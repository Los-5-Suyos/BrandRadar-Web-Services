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
@Table(name = "LoginAttempt")
public class LoginAttemptJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LGA_id")
    private Long id;

    @Column(name = "USU_id")
    private Long userId;

    @Column(name = "LGA_email", nullable = false, length = 255)
    private String email;

    @Column(name = "LGA_ip_address", length = 45)
    private String ipAddress;

    @Column(name = "LGA_success", nullable = false)
    private Boolean success;

    @CreatedDate
    @Column(name = "LGA_attempted_at", nullable = false, updatable = false)
    private Instant attemptedAt;
}