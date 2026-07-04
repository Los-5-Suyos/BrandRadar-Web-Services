package brandradar.brandworkspace.domain.model.aggregates;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Subscription {

    private final Long id;
    private final Long workspaceId;
    private final String plan;
    private final String billingPeriod;
    private final String status;
    private final String fakeCardLast4;
    private final String fakeCardBrand;
    private final Instant startedAt;
    private final Instant renewsAt;

    private Subscription(Long id, Long workspaceId, String plan, String billingPeriod, String status,
                         String fakeCardLast4, String fakeCardBrand, Instant startedAt, Instant renewsAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.plan = plan;
        this.billingPeriod = billingPeriod;
        this.status = status != null ? status : "ACTIVE";
        this.fakeCardLast4 = fakeCardLast4;
        this.fakeCardBrand = fakeCardBrand;
        this.startedAt = startedAt;
        this.renewsAt = renewsAt;
    }

    public static Subscription create(Long workspaceId, String plan, String billingPeriod,
                                      String fakeCardLast4, String fakeCardBrand) {
        var now = Instant.now();
        long months = "ANUAL".equalsIgnoreCase(billingPeriod) ? 12 : 1;
        return new Subscription(null, workspaceId, plan, billingPeriod, "ACTIVE",
                fakeCardLast4, fakeCardBrand, now, now.plus(months * 30L, ChronoUnit.DAYS));
    }

    public static Subscription rehydrate(Long id, Long workspaceId, String plan, String billingPeriod,
                                         String status, String fakeCardLast4, String fakeCardBrand,
                                         Instant startedAt, Instant renewsAt) {
        return new Subscription(id, workspaceId, plan, billingPeriod, status, fakeCardLast4,
                fakeCardBrand, startedAt, renewsAt);
    }

    public Subscription cancel() {
        return new Subscription(this.id, this.workspaceId, "FREE", this.billingPeriod, "CANCELED",
                this.fakeCardLast4, this.fakeCardBrand, this.startedAt, null);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getPlan() { return plan; }
    public String getBillingPeriod() { return billingPeriod; }
    public String getStatus() { return status; }
    public String getFakeCardLast4() { return fakeCardLast4; }
    public String getFakeCardBrand() { return fakeCardBrand; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getRenewsAt() { return renewsAt; }
}