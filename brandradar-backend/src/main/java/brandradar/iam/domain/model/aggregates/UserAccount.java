package brandradar.iam.domain.model.aggregates;

import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;

import java.time.Instant;
import java.util.Objects;

public class UserAccount {

    private final Long id;
    private final Email email;
    private final PasswordHash passwordHash;
    private final String role;
    private final String description;
    private final String status;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String passwordRecoveryToken;
    private final Instant tokenExpiryDate;
    private final int sessionVersion;

    private UserAccount(Long id, Email email, PasswordHash passwordHash,
                        String role, String description, String status,
                        Instant createdAt, Instant updatedAt,
                        String passwordRecoveryToken, Instant tokenExpiryDate, int sessionVersion) {
        this.id = id;
        this.email = Objects.requireNonNull(email, "Email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.description = description;
        this.status = status != null ? status : "PENDING_VERIFICATION";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.passwordRecoveryToken = passwordRecoveryToken;
        this.tokenExpiryDate = tokenExpiryDate;
        this.sessionVersion = sessionVersion;
    }

    public static UserAccount create(Email email, PasswordHash passwordHash, String role, String description) {
        return new UserAccount(null, email, passwordHash, role, description, "PENDING_VERIFICATION",
                Instant.now(), Instant.now(), null, null, 0);
    }

    public static UserAccount rehydrate(Long id, Email email, PasswordHash passwordHash, String role, String description, String status, Instant createdAt, Instant updatedAt,
                                        String passwordRecoveryToken, Instant tokenExpiryDate, int sessionVersion) {
        return new UserAccount(id, email, passwordHash, role, description, status, createdAt, updatedAt,
                passwordRecoveryToken, tokenExpiryDate, sessionVersion);
    }

    /**
     * T-07: Inicia el flujo de recuperación generando un clon con el token y 15 min de vida.
     */
    public UserAccount withPasswordRecoveryToken(String token) {
        return new UserAccount(
                this.id, this.email, this.passwordHash, this.role, this.description, this.status,
                this.createdAt, Instant.now(), // Actualiza la fecha de modificación
                token,
                Instant.now().plusSeconds(15 * 60), // Define expiración exacta en 15 minutos
                this.sessionVersion
        );
    }

    /**
     * T-08: Completa el cambio de contraseña, limpia el token e incrementa sessionVersion.
     */
    public UserAccount withUpdatedPassword(PasswordHash newPasswordHash) {
        return new UserAccount(
                this.id, this.email, newPasswordHash, this.role, this.description, this.status,
                this.createdAt, Instant.now(),
                null, // Limpia el token de recuperación ya utilizado
                null, // Limpia la fecha de expiración
                this.sessionVersion + 1 // Incrementa la versión para invalidar las sesiones activas
        );
    }

    public Long getId() { return id; }
    public Email getEmail() { return email; }
    public PasswordHash getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public String getPasswordRecoveryToken() { return passwordRecoveryToken; }
    public Instant getTokenExpiryDate() { return tokenExpiryDate; }
    public int getSessionVersion() { return sessionVersion; }
}