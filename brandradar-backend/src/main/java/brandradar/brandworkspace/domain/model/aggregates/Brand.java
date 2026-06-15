package brandradar.brandworkspace.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Brand {

    private final Long id;
    private final Long workspaceId;
    private final String name;
    private final BigDecimal reputationScore;
    private final Instant reputationCalculatedAt;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Brand(Long id, Long workspaceId, String name, BigDecimal reputationScore,
                  Instant reputationCalculatedAt, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.workspaceId = Objects.requireNonNull(workspaceId, "WorkspaceId is required");
        this.name = Objects.requireNonNull(name, "Name is required");
        this.reputationScore = reputationScore != null ? reputationScore : BigDecimal.ZERO;
        this.reputationCalculatedAt = reputationCalculatedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Brand create(Long workspaceId, String name) {
        return new Brand(null, workspaceId, name, BigDecimal.ZERO, null, null, null);
    }

    public static Brand rehydrate(Long id, Long workspaceId, String name, BigDecimal reputationScore,
                                  Instant reputationCalculatedAt, Instant createdAt, Instant updatedAt) {
        return new Brand(id, workspaceId, name, reputationScore, reputationCalculatedAt, createdAt, updatedAt);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getName() { return name; }
    public BigDecimal getReputationScore() { return reputationScore; }
    public Instant getReputationCalculatedAt() { return reputationCalculatedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}