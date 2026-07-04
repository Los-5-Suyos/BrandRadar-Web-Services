package brandradar.iam.infrastructure.persistence.jpa.entities;

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

    @Column(name = "USU_full_name", length = 255)
    private String fullName;

    @Column(name = "USU_username", unique = true, length = 100)
    private String username;

    @Column(name = "USU_password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "USU_role", nullable = false, length = 20)
    private String role;

    @Column(name = "USU_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "USU_avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "USU_bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "USU_language", length = 5)
    private String language;

    @Column(name = "USU_timezone", length = 20)
    private String timezone;

    @Column(name = "USU_email_notifications")
    private Boolean emailNotifications;

    @Column(name = "USU_status", nullable = false, length = 30)
    private String status;

    @Column(name = "USU_verification_code", length = 6)
    private String verificationCode;

    @CreatedDate
    @Column(name = "USU_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "USU_updated_at", nullable = false)
    private Instant updatedAt;

    public UserAccountJpaEntity(Long id, String email, String passwordHash,
                                String role, String description, String status, String verificationCode) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.description = description;
        this.status = status;
        this.verificationCode = verificationCode;
    }

    public UserAccountJpaEntity(Long id, String email, String passwordHash,
                                String role, String description, String status) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.description = description;
        this.status = status;
    }

    public UserAccountJpaEntity(Long id, String email, String fullName, String username,
                                String passwordHash, String role, String description,
                                String avatarUrl, String bio, String language, String timezone,
                                Boolean emailNotifications, String status, String verificationCode) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.description = description;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.language = language;
        this.timezone = timezone;
        this.emailNotifications = emailNotifications;
        this.status = status;
        this.verificationCode = verificationCode;
    }
}