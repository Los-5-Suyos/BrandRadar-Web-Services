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

    private UserAccount(Long id, Email email, PasswordHash passwordHash,
                        String role, String description, String status,
                        Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email, "Email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.description = description;
        this.status = status != null ? status : "PENDING_VERIFICATION";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserAccount create(Email email, PasswordHash passwordHash, String role, String description) {
        return new UserAccount(null, email, passwordHash, role, description, "PENDING_VERIFICATION", null, null);
    }

    public static UserAccount rehydrate(Long id, Email email, PasswordHash passwordHash, String role, String description, String status, Instant createdAt, Instant updatedAt) {
        return new UserAccount(id, email, passwordHash, role, description, status, createdAt, updatedAt);
    }

    public Long getId() { return id; }
    public Email getEmail() { return email; }
    public PasswordHash getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}