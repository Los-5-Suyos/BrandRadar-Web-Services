package brandradar.iam.infrastructure.persistence.jpa.entities;

import brandradar.shared.infrastructure.persistence.jpa.audit.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "UserAccount")
public class UserAccountJpaEntity extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USU_id")
    private Long id;

    @Column(name = "USU_email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "USU_password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "USU_role", nullable = false, length = 20)
    private String role;

    @Column(name = "USU_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "USU_status", nullable = false, length = 30)
    private String status;

    @Column(name = "USU_failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    public UserAccountJpaEntity(Long id, String email, String passwordHash,
                                String role, String description, String status,
                                int failedLoginAttempts) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.description = description;
        this.status = status;
        this.failedLoginAttempts = failedLoginAttempts;
    }
}