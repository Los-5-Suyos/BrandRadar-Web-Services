package brandradar.iam.domain.model.aggregates;

import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;

import java.time.Instant;
import java.util.Objects;

public class UserAccount {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    public static final String STATUS_PENDING   = "PENDING_VERIFICATION";
    public static final String STATUS_ACTIVE    = "ACTIVE";
    public static final String STATUS_BLOCKED   = "BLOCKED";

    private final Long id;
    private final Email email;
    private final PasswordHash passwordHash;
    private final String role;
    private final String description;
    private String status;
    private int failedLoginAttempts;
    private final Instant createdAt;
    private final Instant updatedAt;
    
    // Campos para recuperación de contraseña y control de sesión
    private final String passwordRecoveryToken;
    private final Instant tokenExpiryDate;
    private final Long sessionVersion;

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

    public void activate() {
        this.status = STATUS_ACTIVE;
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
