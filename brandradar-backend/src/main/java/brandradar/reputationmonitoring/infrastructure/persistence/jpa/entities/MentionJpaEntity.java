package brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Mention")
public class MentionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEN_id")
    private Long id;

    @Column(name = "MES_id")
    private Long mentionStreamId;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "MEN_content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "MEN_source_platform", length = 100)
    private String sourcePlatform;

    @Column(name = "MEN_source_url", length = 500)
    private String sourceUrl;

    @Column(name = "MEN_source_reliability", nullable = false)
    private BigDecimal sourceReliability;

    @Column(name = "MEN_author_name", length = 255)
    private String author;

    @Column(name = "MEN_author_handle", length = 255)
    private String authorHandle;

    @Column(name = "MEN_published_at")
    private Instant publishedAt;

    @Column(name = "MEN_category", length = 20)
    private String category;

    @Column(name = "MEN_sentiment_positive", nullable = false)
    private BigDecimal sentimentPositive;

    @Column(name = "MEN_sentiment_negative", nullable = false)
    private BigDecimal sentimentNegative;

    @Column(name = "MEN_sentiment_neutral", nullable = false)
    private BigDecimal sentimentNeutral;

    @Column(name = "MEN_sentiment_compound", nullable = false)
    private BigDecimal sentimentCompound;

    @Column(name = "MEN_sentiment_confidence", nullable = false)
    private BigDecimal sentimentConfidence;

    @Column(name = "MEN_engagement_likes")
    private Integer engagementLikes;

    @Column(name = "MEN_engagement_comments")
    private Integer engagementComments;

    @Column(name = "MEN_engagement_views")
    private Integer engagementViews;

    @Column(name = "MEN_status", nullable = false, length = 20)
    private String status;

    @CreatedDate
    @Column(name = "MEN_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public MentionJpaEntity(Long id, Long mentionStreamId, Long brandId, String content,
                            String sourcePlatform, String sourceUrl, BigDecimal sourceReliability,
                            String author, String authorHandle, Instant publishedAt, String category,
                            BigDecimal sentimentPositive, BigDecimal sentimentNegative,
                            BigDecimal sentimentNeutral, BigDecimal sentimentCompound,
                            BigDecimal sentimentConfidence, Integer engagementLikes,
                            Integer engagementComments, Integer engagementViews, String status) {
        this.id = id;
        this.mentionStreamId = mentionStreamId;
        this.brandId = brandId;
        this.content = content;
        this.sourcePlatform = sourcePlatform;
        this.sourceUrl = sourceUrl;
        this.sourceReliability = sourceReliability;
        this.author = author;
        this.authorHandle = authorHandle;
        this.publishedAt = publishedAt;
        this.category = category;
        this.sentimentPositive = sentimentPositive;
        this.sentimentNegative = sentimentNegative;
        this.sentimentNeutral = sentimentNeutral;
        this.sentimentCompound = sentimentCompound;
        this.sentimentConfidence = sentimentConfidence;
        this.engagementLikes = engagementLikes != null ? engagementLikes : 0;
        this.engagementComments = engagementComments != null ? engagementComments : 0;
        this.engagementViews = engagementViews != null ? engagementViews : 0;
        this.status = status != null ? status : "PENDIENTE";
    }
}