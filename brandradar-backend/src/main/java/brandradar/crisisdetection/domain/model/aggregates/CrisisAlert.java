package brandradar.crisisdetection.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class CrisisAlert {

    private final Long id;
    private final Long brandId;
    private final Long mentionStreamId;
    private final Long monitoringRuleId;
    private final Integer priorityLevel;
    private final String priorityLabel;
    private final String status;
    private final String triggerType;
    private final BigDecimal triggerDeviationPct;
    private final BigDecimal triggerConfidence;
    private final Instant detectedAt;
    private final Instant acknowledgedAt;
    private final String dismissedReason;
    private final Integer responseTimeMinutes;

    private CrisisAlert(Long id, Long brandId, Long mentionStreamId, Long monitoringRuleId,
                        Integer priorityLevel, String priorityLabel, String status,
                        String triggerType, BigDecimal triggerDeviationPct, BigDecimal triggerConfidence,
                        Instant detectedAt, Instant acknowledgedAt, String dismissedReason,
                        Integer responseTimeMinutes) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "BrandId is required");
        this.mentionStreamId = mentionStreamId;
        this.monitoringRuleId = monitoringRuleId;
        this.priorityLevel = priorityLevel != null ? priorityLevel : 1;
        this.priorityLabel = priorityLabel;
        this.status = status != null ? status : "OPEN";
        this.triggerType = triggerType;
        this.triggerDeviationPct = triggerDeviationPct;
        this.triggerConfidence = triggerConfidence;
        this.detectedAt = detectedAt;
        this.acknowledgedAt = acknowledgedAt;
        this.dismissedReason = dismissedReason;
        this.responseTimeMinutes = responseTimeMinutes;
    }

    public static CrisisAlert create(Long brandId, Long mentionStreamId, Long monitoringRuleId,
                                     Integer priorityLevel, String priorityLabel, String triggerType,
                                     BigDecimal triggerDeviationPct, BigDecimal triggerConfidence) {
        return new CrisisAlert(null, brandId, mentionStreamId, monitoringRuleId, priorityLevel,
                priorityLabel, "OPEN", triggerType, triggerDeviationPct, triggerConfidence,
                Instant.now(), null, null, null);
    }

    public static CrisisAlert rehydrate(Long id, Long brandId, Long mentionStreamId, Long monitoringRuleId,
                                        Integer priorityLevel, String priorityLabel, String status,
                                        String triggerType, BigDecimal triggerDeviationPct,
                                        BigDecimal triggerConfidence, Instant detectedAt,
                                        Instant acknowledgedAt, String dismissedReason,
                                        Integer responseTimeMinutes) {
        return new CrisisAlert(id, brandId, mentionStreamId, monitoringRuleId, priorityLevel,
                priorityLabel, status, triggerType, triggerDeviationPct, triggerConfidence,
                detectedAt, acknowledgedAt, dismissedReason, responseTimeMinutes);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public Long getMentionStreamId() { return mentionStreamId; }
    public Long getMonitoringRuleId() { return monitoringRuleId; }
    public Integer getPriorityLevel() { return priorityLevel; }
    public String getPriorityLabel() { return priorityLabel; }
    public String getStatus() { return status; }
    public String getTriggerType() { return triggerType; }
    public BigDecimal getTriggerDeviationPct() { return triggerDeviationPct; }
    public BigDecimal getTriggerConfidence() { return triggerConfidence; }
    public Instant getDetectedAt() { return detectedAt; }
    public Instant getAcknowledgedAt() { return acknowledgedAt; }
    public String getDismissedReason() { return dismissedReason; }
    public Integer getResponseTimeMinutes() { return responseTimeMinutes; }
}