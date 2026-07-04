package brandradar.iam.application.services;

import brandradar.iam.domain.model.aggregates.Notification;
import brandradar.iam.domain.model.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    public static final String TYPE_CRISIS_ALERT = "CRISIS_ALERT";
    public static final String TYPE_SCORE_DROP = "SCORE_DROP";
    public static final String TYPE_POSITIVE_MENTION = "POSITIVE_MENTION";

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void notify(Long userId, Long brandId, String type, String title, String message) {
        var notification = Notification.create(userId, brandId, type, title, message);
        repository.save(notification);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        repository.findById(notificationId)
                .ifPresent(n -> repository.save(n.markRead()));
    }
}