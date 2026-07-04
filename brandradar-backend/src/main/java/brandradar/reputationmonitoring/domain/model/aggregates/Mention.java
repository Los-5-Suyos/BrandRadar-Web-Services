package brandradar.reputationmonitoring.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Mention {

    private final Long id;
    private final Long mentionStreamId;
    private final Long brandId;
    private final String content;
    private final String sourcePlatform;
    private final String sourceUrl;
    private final BigDecimal sourceReliability;
    private final String author;
    private final String authorHandle;
    private final Instant publishedAt;
    private final String category;
    private final BigDecimal sentimentPositive;
    private final BigDecimal sentimentNegative;
    private final BigDecimal sentimentNeutral;
    private final BigDecimal sentimentCompound;
    private final BigDecimal sentimentConfidence;
    private final Integer engagementLikes;
    private final Integer engagementComments;
    private final Integer engagementViews;
    private final String status;
    private final Instant createdAt;

    private Mention(Long id, Long mentionStreamId, Long brandId, String content,
                    String sourcePlatform, String sourceUrl, BigDecimal sourceReliability,
                    String author, String authorHandle, Instant publishedAt, String category,
                    BigDecimal sentimentPositive, BigDecimal sentimentNegative,
                    BigDecimal sentimentNeutral, BigDecimal sentimentCompound,
                    BigDecimal sentimentConfidence, Integer engagementLikes,
                    Integer engagementComments, Integer engagementViews, String status, Instant createdAt) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "BrandId is required");
        this.content = Objects.requireNonNull(content, "Content is required");
        this.mentionStreamId = mentionStreamId;
        this.sourcePlatform = sourcePlatform;
        this.sourceUrl = sourceUrl;
        this.sourceReliability = sourceReliability != null ? sourceReliability : new BigDecimal("0.50");
        this.author = author;
        this.authorHandle = authorHandle;
        this.publishedAt = publishedAt;
        this.category = category;
        this.sentimentPositive = sentimentPositive != null ? sentimentPositive : BigDecimal.ZERO;
        this.sentimentNegative = sentimentNegative != null ? sentimentNegative : BigDecimal.ZERO;
        this.sentimentNeutral = sentimentNeutral != null ? sentimentNeutral : BigDecimal.ZERO;
        this.sentimentCompound = sentimentCompound != null ? sentimentCompound : BigDecimal.ZERO;
        this.sentimentConfidence = sentimentConfidence != null ? sentimentConfidence : BigDecimal.ZERO;
        this.engagementLikes = engagementLikes != null ? engagementLikes : 0;
        this.engagementComments = engagementComments != null ? engagementComments : 0;
        this.engagementViews = engagementViews != null ? engagementViews : 0;
        this.status = status != null ? status : "PENDIENTE";
        this.createdAt = createdAt;
    }

    public static Mention create(Long brandId, String content, String sourcePlatform,
                                 String sourceUrl, String author, Instant publishedAt) {
        return new Mention(null, null, brandId, content, sourcePlatform, sourceUrl,
                new BigDecimal("0.50"), author, null, publishedAt, null,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                0, 0, 0, "PENDIENTE", null);
    }

    public static Mention createFull(Long brandId, String content, String sourcePlatform,
                                     String sourceUrl, String author, String authorHandle,
                                     Instant publishedAt, Integer engagementLikes,
                                     Integer engagementComments, Integer engagementViews) {
        return new Mention(null, null, brandId, content, sourcePlatform, sourceUrl,
                new BigDecimal("0.50"), author, authorHandle, publishedAt, null,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                engagementLikes, engagementComments, engagementViews, "PENDIENTE", null);
    }

    public static Mention rehydrate(Long id, Long mentionStreamId, Long brandId, String content,
                                    String sourcePlatform, String sourceUrl, BigDecimal sourceReliability,
                                    String author, String authorHandle, Instant publishedAt, String category,
                                    BigDecimal sentimentPositive, BigDecimal sentimentNegative,
                                    BigDecimal sentimentNeutral, BigDecimal sentimentCompound,
                                    BigDecimal sentimentConfidence, Integer engagementLikes,
                                    Integer engagementComments, Integer engagementViews, String status,
                                    Instant createdAt) {
        return new Mention(id, mentionStreamId, brandId, content, sourcePlatform, sourceUrl,
                sourceReliability, author, authorHandle, publishedAt, category, sentimentPositive,
                sentimentNegative, sentimentNeutral, sentimentCompound, sentimentConfidence,
                engagementLikes, engagementComments, engagementViews, status, createdAt);
    }

    public Mention withSentiment(BigDecimal positive, BigDecimal negative, BigDecimal neutral,
                                 BigDecimal compound, BigDecimal confidence) {
        return new Mention(this.id, this.mentionStreamId, this.brandId, this.content,
                this.sourcePlatform, this.sourceUrl, this.sourceReliability, this.author,
                this.authorHandle, this.publishedAt, this.category, positive, negative, neutral,
                compound, confidence, this.engagementLikes, this.engagementComments,
                this.engagementViews, this.status, this.createdAt);
    }

    public Mention withStatus(String newStatus) {
        return new Mention(this.id, this.mentionStreamId, this.brandId, this.content,
                this.sourcePlatform, this.sourceUrl, this.sourceReliability, this.author,
                this.authorHandle, this.publishedAt, this.category, this.sentimentPositive,
                this.sentimentNegative, this.sentimentNeutral, this.sentimentCompound,
                this.sentimentConfidence, this.engagementLikes, this.engagementComments,
                this.engagementViews, newStatus, this.createdAt);
    }

    public Long getId() { return id; }
    public Long getMentionStreamId() { return mentionStreamId; }
    public Long getBrandId() { return brandId; }
    public String getContent() { return content; }
    public String getSourcePlatform() { return sourcePlatform; }
    public String getSourceUrl() { return sourceUrl; }
    public BigDecimal getSourceReliability() { return sourceReliability; }
    public String getAuthor() { return author; }
    public String getAuthorHandle() { return authorHandle; }
    public Instant getPublishedAt() { return publishedAt; }
    public String getCategory() { return category; }
    public BigDecimal getSentimentPositive() { return sentimentPositive; }
    public BigDecimal getSentimentNegative() { return sentimentNegative; }
    public BigDecimal getSentimentNeutral() { return sentimentNeutral; }
    public BigDecimal getSentimentCompound() { return sentimentCompound; }
    public BigDecimal getSentimentConfidence() { return sentimentConfidence; }
    public Integer getEngagementLikes() { return engagementLikes; }
    public Integer getEngagementComments() { return engagementComments; }
    public Integer getEngagementViews() { return engagementViews; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}