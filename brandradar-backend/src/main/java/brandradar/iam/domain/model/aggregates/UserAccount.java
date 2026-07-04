package brandradar.iam.domain.model.aggregates;

import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;

import java.time.Instant;
import java.util.Objects;

public class UserAccount {
    private final Long id;
    private final Email email;
    private final String fullName;
    private final String username;
    private final PasswordHash passwordHash;
    private final String role;
    private final String description;
    private final String avatarUrl;
    private final String bio;
    private final String language;
    private final String timezone;
    private final Boolean emailNotifications;
    private final String status;
    private final String verificationCode;
    private final Instant createdAt;
    private final Instant updatedAt;

    private UserAccount(Long id, Email email, String fullName, String username, PasswordHash passwordHash,
                        String role, String description, String avatarUrl, String bio, String language,
                        String timezone, Boolean emailNotifications, String status, String verificationCode,
                        Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email, "Email is required");
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
        this.role = Objects.requireNonNull(role, "Role is required");
        this.description = description;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.language = language != null ? language : "ES";
        this.timezone = timezone != null ? timezone : "America/Lima";
        this.emailNotifications = emailNotifications != null ? emailNotifications : true;
        this.status = status != null ? status : "PENDING_VERIFICATION";
        this.verificationCode = verificationCode != null ? verificationCode : "123456";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserAccount create(Email email, PasswordHash passwordHash, String role, String description) {
        return new UserAccount(null, email, null, null, passwordHash, role, description,
                null, null, null, null, null, "PENDING_VERIFICATION", "123456", null, null);
    }

    public static UserAccount rehydrate(Long id, Email email, String fullName, String username,
                                        PasswordHash passwordHash, String role, String description,
                                        String avatarUrl, String bio, String language, String timezone,
                                        Boolean emailNotifications, String status, String verificationCode,
                                        Instant createdAt, Instant updatedAt) {
        return new UserAccount(id, email, fullName, username, passwordHash, role, description,
                avatarUrl, bio, language, timezone, emailNotifications, status, verificationCode,
                createdAt, updatedAt);
    }

    public UserAccount activate() {
        return new UserAccount(this.id, this.email, this.fullName, this.username, this.passwordHash,
                this.role, this.description, this.avatarUrl, this.bio, this.language, this.timezone,
                this.emailNotifications, "ACTIVE", this.verificationCode, this.createdAt, this.updatedAt);
    }

    /** Combina esta cuenta con los campos nuevos de un PATCH de perfil — null = no cambiar. */
    public UserAccount withProfileUpdates(String fullName, String bio, String language,
                                          String timezone, Boolean emailNotifications, String avatarUrl) {
        return new UserAccount(this.id, this.email, fullName != null ? fullName : this.fullName,
                this.username, this.passwordHash, this.role, this.description,
                avatarUrl != null ? avatarUrl : this.avatarUrl,
                bio != null ? bio : this.bio,
                language != null ? language : this.language,
                timezone != null ? timezone : this.timezone,
                emailNotifications != null ? emailNotifications : this.emailNotifications,
                this.status, this.verificationCode, this.createdAt, this.updatedAt);
    }

    public UserAccount withNewPasswordHash(PasswordHash newHash) {
        return new UserAccount(this.id, this.email, this.fullName, this.username, newHash,
                this.role, this.description, this.avatarUrl, this.bio, this.language, this.timezone,
                this.emailNotifications, this.status, this.verificationCode, this.createdAt, this.updatedAt);
    }

    public Long getId() { return id; }
    public Email getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public PasswordHash getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getDescription() { return description; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getBio() { return bio; }
    public String getLanguage() { return language; }
    public String getTimezone() { return timezone; }
    public Boolean getEmailNotifications() { return emailNotifications; }
    public String getStatus() { return status; }
    public String getVerificationCode() { return verificationCode; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}