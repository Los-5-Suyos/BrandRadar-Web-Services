package brandradar.iam.domain.model.aggregates;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class EmailVerification {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_USED = "USED";
    public static final String STATUS_EXPIRED = "EXPIRED";

    private final Long id;
    private final Long userId;
    private final String token;
    private String status;
    private final Instant expiresAt;
    private Instant usedAt;
    private final Instant createdAt;

    private EmailVerification(Long id, Long userId, String token, String status, Instant expiresAt, Instant usedAt, Instant createdAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "UserId is required");
        this.token = Objects.requireNonNull(token, "Token is required");
        this.status = status != null ? status : STATUS_PENDING;
        this.expiresAt = Objects.requireNonNull(expiresAt, "ExpiresAt is required");
        this.usedAt = usedAt;
        this.createdAt = createdAt;
    }

    public static EmailVerification create(Long userId, Instant expiresAt) {
        return new EmailVerification(null, userId, UUID.randomUUID().toString(), STATUS_PENDING, expiresAt, null, Instant.now());
    }

    public static EmailVerification rehydrate(Long id, Long userId, String token, String status, Instant expiresAt, Instant usedAt, Instant createdAt) {
        return new EmailVerification(id, userId, token, status, expiresAt, usedAt, createdAt);
    }

    public void markAsUsed() {
        if (!STATUS_PENDING.equals(this.status)) {
            throw new IllegalStateException("Token is not pending");
        }
        if (Instant.now().isAfter(this.expiresAt)) {
            this.status = STATUS_EXPIRED;
            throw new IllegalStateException("Token is expired");
        }
        this.status = STATUS_USED;
        this.usedAt = Instant.now();
    }

    public boolean isExpired() {
        return STATUS_EXPIRED.equals(this.status) || Instant.now().isAfter(this.expiresAt);
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getToken() { return token; }
    public String getStatus() { return status; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getUsedAt() { return usedAt; }
    public Instant getCreatedAt() { return createdAt; }
}
