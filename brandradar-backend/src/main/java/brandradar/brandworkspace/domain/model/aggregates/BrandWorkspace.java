package brandradar.brandworkspace.domain.model.aggregates;

import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceName;

import java.time.Instant;
import java.util.Objects;

public class BrandWorkspace {

    private final Long id;
    private final Long userId;
    private final WorkspaceName name;
    private final String plan;
    private final String status;
    private final Integer policyMaxBrands;
    private final Integer policyAlertQuota;
    private final Instant createdAt;
    private final Instant updatedAt;

    private BrandWorkspace(Long id, Long userId, WorkspaceName name, String plan,
                           String status, Integer policyMaxBrands, Integer policyAlertQuota,
                           Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "UserId is required");
        this.name = Objects.requireNonNull(name, "Name is required");
        this.plan = plan != null ? plan : "FREE";
        this.status = status != null ? status : "WITHOUT_CONFIGURATION";
        this.policyMaxBrands = policyMaxBrands != null ? policyMaxBrands : 3;
        this.policyAlertQuota = policyAlertQuota != null ? policyAlertQuota : 100;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BrandWorkspace create(Long userId, WorkspaceName name, String plan) {
        return new BrandWorkspace(null, userId, name, plan, "WITHOUT_CONFIGURATION", 3, 100, null, null);
    }

    public static BrandWorkspace rehydrate(Long id, Long userId, WorkspaceName name, String plan,
                                           String status, Integer policyMaxBrands, Integer policyAlertQuota,
                                           Instant createdAt, Instant updatedAt) {
        return new BrandWorkspace(id, userId, name, plan, status, policyMaxBrands, policyAlertQuota, createdAt, updatedAt);
    }

    /** Combina esta instancia con los campos nuevos de un PATCH — null significa "no cambiar". */
    public BrandWorkspace withUpdates(String newName, String newPlan) {
        return new BrandWorkspace(
                this.id,
                this.userId,
                newName != null ? new WorkspaceName(newName) : this.name,
                newPlan != null ? newPlan : this.plan,
                this.status,
                this.policyMaxBrands,
                this.policyAlertQuota,
                this.createdAt,
                this.updatedAt
        );
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public WorkspaceName getName() { return name; }
    public String getPlan() { return plan; }
    public String getStatus() { return status; }
    public Integer getPolicyMaxBrands() { return policyMaxBrands; }
    public Integer getPolicyAlertQuota() { return policyAlertQuota; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}