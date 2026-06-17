package brandradar.reputationmonitoring.domain.model.aggregates;

import java.time.Instant;
import java.util.Objects;

public class IncidentEvent {

    private final Long id;
    private final Long incidentId;
    private final String eventType;
    private final String status;
    private final Long performedBy;
    private final Instant occurredAt;

    private IncidentEvent(Long id, Long incidentId, String eventType,
                          String status, Long performedBy, Instant occurredAt) {
        this.id = id;
        this.incidentId = Objects.requireNonNull(incidentId, "IncidentId is required");
        this.eventType = Objects.requireNonNull(eventType, "EventType is required");
        this.status = status != null ? status : "DONE";
        this.performedBy = performedBy;
        this.occurredAt = occurredAt;
    }

    public static IncidentEvent create(Long incidentId, String eventType, Long performedBy) {
        return new IncidentEvent(null, incidentId, eventType, "DONE", performedBy, null);
    }

    public static IncidentEvent rehydrate(Long id, Long incidentId, String eventType,
                                          String status, Long performedBy, Instant occurredAt) {
        return new IncidentEvent(id, incidentId, eventType, status, performedBy, occurredAt);
    }

    public Long getId() { return id; }
    public Long getIncidentId() { return incidentId; }
    public String getEventType() { return eventType; }
    public String getStatus() { return status; }
    public Long getPerformedBy() { return performedBy; }
    public Instant getOccurredAt() { return occurredAt; }
}