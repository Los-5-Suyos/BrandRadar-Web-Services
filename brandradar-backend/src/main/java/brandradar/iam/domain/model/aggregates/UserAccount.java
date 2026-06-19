package brandradar.iam.domain.model.aggregates;

import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "UserAccount")
public class UserAccount {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    public static final String STATUS_PENDING   = "PENDING_VERIFICATION";
    public static final String STATUS_ACTIVE    = "ACTIVE";
    public static final String STATUS_BLOCKED   = "BLOCKED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USU_id")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "USU_email"))
    })
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "USU_password_hash"))
    })
    private PasswordHash passwordHash;

    @Column(name = "USU_role")
    private String role;

    @Column(name = "USU_description")
    private String description;

    @Column(name = "USU_status")
    private String status;

    @Column(name = "USU_failed_login_attempts")
    private int failedLoginAttempts;

    @Column(name = "USU_created_at")
    private Instant createdAt;

    @Column(name = "USU_updated_at")
    private Instant updatedAt;

    @Column(name = "USU_password_recovery_token")
    private String passwordRecoveryToken;

    @Column(name = "USU_token_expiry_date")
    private Instant tokenExpiryDate;

    @Column(name = "USU_session_version")
    private Long sessionVersion;

    protected UserAccount() {
        this.id = null;
        this.email = null;
        this.passwordHash = null;
        this.role = null;
        this.description = null;
        this.status = null;
        this.failedLoginAttempts = 0;
        this.createdAt = null;
        this.updatedAt = null;
        this.passwordRecoveryToken = null;
        this.tokenExpiryDate = null;
        this.sessionVersion = null;
    }


    private UserAccount(Long id, Email email, PasswordHash passwordHash,
                        String role, String description, String status,
                        int failedLoginAttempts, Instant createdAt, Instant updatedAt,
                        String passwordRecoveryToken, Instant tokenExpiryDate, Long sessionVersion) {
        this.id = id;
        this.email = Objects.requireNonNull(email, "Email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.description = description;
        this.status = status != null ? status : STATUS_PENDING;
        this.failedLoginAttempts = failedLoginAttempts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.passwordRecoveryToken = passwordRecoveryToken;
        this.tokenExpiryDate = tokenExpiryDate;
        this.sessionVersion = sessionVersion != null ? sessionVersion : 0L;
    }

    public static UserAccount create(Email email, PasswordHash passwordHash, String role, String description) {
        return new UserAccount(null, email, passwordHash, role, description, STATUS_PENDING, 0, 
                Instant.now(), Instant.now(), null, null, 0L);
    }

    public static UserAccount rehydrate(Long id, Email email, PasswordHash passwordHash,
                                        String role, String description, String status,
                                        int failedLoginAttempts, Instant createdAt, Instant updatedAt,
                                        String passwordRecoveryToken, Instant tokenExpiryDate, Long sessionVersion) {
        return new UserAccount(id, email, passwordHash, role, description, status, failedLoginAttempts, 
                createdAt, updatedAt, passwordRecoveryToken, tokenExpiryDate, sessionVersion);
    }

    // ── Comportamiento de dominio ──────────────────────────────────────────

    public void incrementFailedAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
            this.status = STATUS_BLOCKED;
        }
    }

    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
    }

    public boolean isActive() {
        return STATUS_ACTIVE.equals(this.status);
    }

    public boolean isBlocked() {
        return STATUS_BLOCKED.equals(this.status);
    }

    public boolean isPendingVerification() {
        return STATUS_PENDING.equals(this.status);
    }

    public UserAccount withPasswordRecoveryToken(String token) {
        return new UserAccount(
                this.id, this.email, this.passwordHash, this.role, this.description, this.status,
                this.failedLoginAttempts,
                this.createdAt, Instant.now(),
                token,
                Instant.now().plusSeconds(15 * 60),
                this.sessionVersion
        );
    }

    public UserAccount withUpdatedPassword(PasswordHash newPasswordHash) {
        return new UserAccount(
                this.id, this.email, newPasswordHash, this.role, this.description, this.status,
                this.failedLoginAttempts,
                this.createdAt, Instant.now(),
                null,
                null,
                this.sessionVersion + 1
        );
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public Email getEmail() { return email; }
    public PasswordHash getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public String getPasswordRecoveryToken() { return passwordRecoveryToken; }
    public Instant getTokenExpiryDate() { return tokenExpiryDate; }
    public Long getSessionVersion() { return sessionVersion; }
}
