package brandradar.iam.infrastructure.persistence.jpa.entities;

import brandradar.shared.infrastructure.persistence.jpa.audit.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "UserAccount")
public class UserAccountJpaEntity {

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

    @CreatedDate
    @Column(name = "USU_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "USU_updated_at", nullable = false)
    private Instant updatedAt;

    public UserAccountJpaEntity(Long id, String email, String passwordHash,
                                String role, String description, String status) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.description = description;
        this.status = status;
    }
}