package brandradar.iam.domain.model.aggregates;

import java.time.Instant;

public class Notification {

    private final Long id;
    private final Long userId;
    private final Long brandId;
    private final String type;
    private final String title;
    private final String message;
    private final Boolean isRead;
    private final Instant createdAt;

    private Notification(Long id, Long userId, Long brandId, String type, String title,
                         String message, Boolean isRead, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.brandId = brandId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public static Notification create(Long userId, Long brandId, String type, String title, String message) {
        return new Notification(null, userId, brandId, type, title, message, false, null);
    }

    public static Notification rehydrate(Long id, Long userId, Long brandId, String type, String title,
                                         String message, Boolean isRead, Instant createdAt) {
        return new Notification(id, userId, brandId, type, title, message, isRead, createdAt);
    }

    public Notification markRead() {
        return new Notification(this.id, this.userId, this.brandId, this.type, this.title,
                this.message, true, this.createdAt);
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getBrandId() { return brandId; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Boolean getIsRead() { return isRead; }
    public Instant getCreatedAt() { return createdAt; }
}