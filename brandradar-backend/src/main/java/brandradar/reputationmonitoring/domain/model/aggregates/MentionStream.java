package brandradar.reputationmonitoring.domain.model.aggregates;

import java.time.Instant;
import java.util.Objects;

public class MentionStream {

    private final Long id;
    private final Long brandId;
    private final Instant periodFrom;
    private final Instant periodTo;
    private final String status;
    private final Instant createdAt;
    private final Instant updatedAt;

    private MentionStream(Long id, Long brandId, Instant periodFrom, Instant periodTo,
                          String status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "BrandId is required");
        this.periodFrom = Objects.requireNonNull(periodFrom, "PeriodFrom is required");
        this.periodTo = Objects.requireNonNull(periodTo, "PeriodTo is required");
        this.status = status != null ? status : "PROCESSING";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MentionStream create(Long brandId, Instant periodFrom, Instant periodTo) {
        return new MentionStream(null, brandId, periodFrom, periodTo, "PROCESSING", null, null);
    }

    public static MentionStream rehydrate(Long id, Long brandId, Instant periodFrom, Instant periodTo,
                                          String status, Instant createdAt, Instant updatedAt) {
        return new MentionStream(id, brandId, periodFrom, periodTo, status, createdAt, updatedAt);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public Instant getPeriodFrom() { return periodFrom; }
    public Instant getPeriodTo() { return periodTo; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}