package brandradar.iam.interfaces.rest;

import brandradar.iam.application.services.NotificationService;
import brandradar.iam.domain.model.repositories.NotificationRepository;
import brandradar.iam.interfaces.rest.resources.NotificationResource;
import brandradar.shared.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Notificaciones de la campanita (crisis, caídas de score, menciones positivas)")
public class NotificationsController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final CurrentUser currentUser;

    public NotificationsController(NotificationRepository notificationRepository,
                                   NotificationService notificationService,
                                   CurrentUser currentUser) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.currentUser = currentUser;
    }

    @Operation(summary = "List my notifications, most recent first")
    @GetMapping("/user-accounts/{userId}/notifications")
    public ResponseEntity<List<NotificationResource>> getMyNotifications(@PathVariable Long userId) {
        var me = currentUser.get();
        if (!"ADMIN".equals(me.role()) && !me.userId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot view another user's notifications");
        }
        var notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(n -> new NotificationResource(n.getId(), n.getBrandId(), n.getType(),
                        n.getTitle(), n.getMessage(), n.getIsRead(), n.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Mark a notification as read")
    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        var notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        var me = currentUser.get();
        if (!"ADMIN".equals(me.role()) && !me.userId().equals(notification.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot modify another user's notification");
        }
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}