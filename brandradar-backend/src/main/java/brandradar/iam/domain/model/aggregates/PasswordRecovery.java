package brandradar.iam.domain.model.aggregates;

import java.time.Instant;

public class PasswordRecovery {

    private final Long id;
    private final Long userId;
    private final String token;
    private final String status;
    private final Instant expiresAt;
    private final Instant usedAt;

    private PasswordRecovery(Long id, Long userId, String token, String status,
                             Instant expiresAt, Instant usedAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
    }

    public static PasswordRecovery create(Long userId, String token, Instant expiresAt) {
        return new PasswordRecovery(null, userId, token, "PENDING", expiresAt, null);
    }

    public static PasswordRecovery rehydrate(Long id, Long userId, String token, String status,
                                             Instant expiresAt, Instant usedAt) {
        return new PasswordRecovery(id, userId, token, status, expiresAt, usedAt);
    }

    public PasswordRecovery markUsed() {
        return new PasswordRecovery(this.id, this.userId, this.token, "USED", this.expiresAt, Instant.now());
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isUsable() {
        return "PENDING".equals(status) && !isExpired();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getToken() { return token; }
    public String getStatus() { return status; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getUsedAt() { return usedAt; }
}