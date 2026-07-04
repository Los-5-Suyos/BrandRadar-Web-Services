package brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ReputationReport")
public class ReputationReportJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RPR_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "BRA_id")
    private Long brandId;

    @Column(name = "RPR_title", nullable = false, length = 255)
    private String title;

    @Column(name = "RPR_period_from", nullable = false)
    private Instant periodFrom;

    @Column(name = "RPR_period_to", nullable = false)
    private Instant periodTo;

    @Column(name = "RPR_status", nullable = false, length = 20)
    private String status;

    @Column(name = "RPR_format", nullable = false, length = 10)
    private String format;

    @Column(name = "RPR_file_url", length = 500)
    private String fileUrl;

    @Column(name = "RPR_file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "RPR_recipients_count")
    private Integer recipientsCount;

    @Column(name = "RPR_generated_by")
    private Long generatedBy;

    @Column(name = "RPR_generated_at")
    private Instant generatedAt;

    @CreatedDate
    @Column(name = "RPR_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ReputationReportJpaEntity(Long id, Long workspaceId, Long brandId, String title,
                                     Instant periodFrom, Instant periodTo, String status, String format,
                                     String fileUrl, Long fileSizeBytes, Integer recipientsCount,
                                     Long generatedBy, Instant generatedAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.brandId = brandId;
        this.title = title;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.status = status;
        this.format = format;
        this.fileUrl = fileUrl;
        this.fileSizeBytes = fileSizeBytes;
        this.recipientsCount = recipientsCount != null ? recipientsCount : 0;
        this.generatedBy = generatedBy;
        this.generatedAt = generatedAt;
    }
}