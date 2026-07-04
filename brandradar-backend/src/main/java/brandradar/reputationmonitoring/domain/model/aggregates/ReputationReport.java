package brandradar.reputationmonitoring.domain.model.aggregates;

import java.time.Instant;

public class ReputationReport {

    private final Long id;
    private final Long workspaceId;
    private final Long brandId;
    private final String title;
    private final Instant periodFrom;
    private final Instant periodTo;
    private final String status;
    private final String format;
    private final String fileUrl;
    private final Long fileSizeBytes;
    private final Integer recipientsCount;
    private final Long generatedBy;
    private final Instant generatedAt;
    private final Instant createdAt;

    private ReputationReport(Long id, Long workspaceId, Long brandId, String title, Instant periodFrom,
                             Instant periodTo, String status, String format, String fileUrl,
                             Long fileSizeBytes, Integer recipientsCount, Long generatedBy,
                             Instant generatedAt, Instant createdAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.brandId = brandId;
        this.title = title;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.status = status != null ? status : "GENERATING";
        this.format = format;
        this.fileUrl = fileUrl;
        this.fileSizeBytes = fileSizeBytes;
        this.recipientsCount = recipientsCount != null ? recipientsCount : 0;
        this.generatedBy = generatedBy;
        this.generatedAt = generatedAt;
        this.createdAt = createdAt;
    }

    public static ReputationReport create(Long workspaceId, Long brandId, String title,
                                          Instant periodFrom, Instant periodTo, String format, Long generatedBy) {
        return new ReputationReport(null, workspaceId, brandId, title, periodFrom, periodTo,
                "GENERATING", format, null, null, 0, generatedBy, null, null);
    }

    public static ReputationReport rehydrate(Long id, Long workspaceId, Long brandId, String title,
                                             Instant periodFrom, Instant periodTo, String status, String format,
                                             String fileUrl, Long fileSizeBytes, Integer recipientsCount,
                                             Long generatedBy, Instant generatedAt, Instant createdAt) {
        return new ReputationReport(id, workspaceId, brandId, title, periodFrom, periodTo, status, format,
                fileUrl, fileSizeBytes, recipientsCount, generatedBy, generatedAt, createdAt);
    }

    public ReputationReport markReady(String fileUrl, long fileSizeBytes) {
        return new ReputationReport(this.id, this.workspaceId, this.brandId, this.title, this.periodFrom,
                this.periodTo, "READY", this.format, fileUrl, fileSizeBytes, this.recipientsCount,
                this.generatedBy, Instant.now(), this.createdAt);
    }

    public ReputationReport markFailed() {
        return new ReputationReport(this.id, this.workspaceId, this.brandId, this.title, this.periodFrom,
                this.periodTo, "FAILED", this.format, this.fileUrl, this.fileSizeBytes, this.recipientsCount,
                this.generatedBy, this.generatedAt, this.createdAt);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public Long getBrandId() { return brandId; }
    public String getTitle() { return title; }
    public Instant getPeriodFrom() { return periodFrom; }
    public Instant getPeriodTo() { return periodTo; }
    public String getStatus() { return status; }
    public String getFormat() { return format; }
    public String getFileUrl() { return fileUrl; }
    public Long getFileSizeBytes() { return fileSizeBytes; }
    public Integer getRecipientsCount() { return recipientsCount; }
    public Long getGeneratedBy() { return generatedBy; }
    public Instant getGeneratedAt() { return generatedAt; }
    public Instant getCreatedAt() { return createdAt; }
}