package brandradar.iam.domain.model.aggregates;

import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;

import java.time.Instant;
import java.util.Objects;

public class UserAccount {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    public static final String STATUS_PENDING   = "PENDIENTE_VERIFICACION";
    public static final String STATUS_ACTIVE    = "ACTIVA";
    public static final String STATUS_BLOCKED   = "BLOQUEADA";

    private final Long id;
    private final Email email;
    private final PasswordHash passwordHash;
    private final String role;
    private final String description;
    private String status;
    private int failedLoginAttempts;
    private final Instant createdAt;
    private final Instant updatedAt;

    private UserAccount(Long id, Email email, PasswordHash passwordHash,
                        String role, String description, String status,
                        int failedLoginAttempts, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email, "Email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.description = description;
        this.status = status != null ? status : STATUS_PENDING;
        this.failedLoginAttempts = failedLoginAttempts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserAccount create(Email email, PasswordHash passwordHash, String role, String description) {
        return new UserAccount(null, email, passwordHash, role, description, STATUS_PENDING, 0, null, null);
    }

    public static UserAccount rehydrate(Long id, Email email, PasswordHash passwordHash,
                                        String role, String description, String status,
                                        int failedLoginAttempts, Instant createdAt, Instant updatedAt) {
        return new UserAccount(id, email, passwordHash, role, description, status, failedLoginAttempts, createdAt, updatedAt);
    }

    // ── Comportamiento de dominio ──────────────────────────────────────────

    /** Incrementa intentos fallidos. Si llega a 5, bloquea la cuenta. */
    public void incrementFailedAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
            this.status = STATUS_BLOCKED;
        }
    }

    /** Resetea el contador de intentos fallidos tras un login exitoso. */
    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
    }

    /** Retorna true si la cuenta está activa y puede iniciar sesión. */
    public boolean isActive() {
        return STATUS_ACTIVE.equals(this.status);
    }

    /** Retorna true si la cuenta está bloqueada por intentos fallidos. */
    public boolean isBlocked() {
        return STATUS_BLOCKED.equals(this.status);
    }

    /** Retorna true si la cuenta está pendiente de verificación de email. */
    public boolean isPendingVerification() {
        return STATUS_PENDING.equals(this.status);
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
}