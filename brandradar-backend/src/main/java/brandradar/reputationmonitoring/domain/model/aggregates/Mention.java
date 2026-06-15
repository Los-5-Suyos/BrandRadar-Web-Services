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
    private final Instant publishedAt;
    private final String category;
    private final BigDecimal sentimentPositive;
    private final BigDecimal sentimentNegative;
    private final BigDecimal sentimentNeutral;
    private final BigDecimal sentimentCompound;
    private final BigDecimal sentimentConfidence;
    private final Instant createdAt;

    private Mention(Long id, Long mentionStreamId, Long brandId, String content,
                    String sourcePlatform, String sourceUrl, BigDecimal sourceReliability,
                    String author, Instant publishedAt, String category,
                    BigDecimal sentimentPositive, BigDecimal sentimentNegative,
                    BigDecimal sentimentNeutral, BigDecimal sentimentCompound,
                    BigDecimal sentimentConfidence, Instant createdAt) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "BrandId is required");
        this.content = Objects.requireNonNull(content, "Content is required");
        this.mentionStreamId = mentionStreamId;
        this.sourcePlatform = sourcePlatform;
        this.sourceUrl = sourceUrl;
        this.sourceReliability = sourceReliability != null ? sourceReliability : new BigDecimal("0.50");
        this.author = author;
        this.publishedAt = publishedAt;
        this.category = category;
        this.sentimentPositive = sentimentPositive != null ? sentimentPositive : BigDecimal.ZERO;
        this.sentimentNegative = sentimentNegative != null ? sentimentNegative : BigDecimal.ZERO;
        this.sentimentNeutral = sentimentNeutral != null ? sentimentNeutral : BigDecimal.ZERO;
        this.sentimentCompound = sentimentCompound != null ? sentimentCompound : BigDecimal.ZERO;
        this.sentimentConfidence = sentimentConfidence != null ? sentimentConfidence : BigDecimal.ZERO;
        this.createdAt = createdAt;
    }

    public static Mention create(Long brandId, String content, String sourcePlatform,
                                 String sourceUrl, String author, Instant publishedAt) {
        return new Mention(null, null, brandId, content, sourcePlatform, sourceUrl,
                new BigDecimal("0.50"), author, publishedAt, null,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null);
    }

    public static Mention rehydrate(Long id, Long mentionStreamId, Long brandId, String content,
                                    String sourcePlatform, String sourceUrl, BigDecimal sourceReliability,
                                    String author, Instant publishedAt, String category,
                                    BigDecimal sentimentPositive, BigDecimal sentimentNegative,
                                    BigDecimal sentimentNeutral, BigDecimal sentimentCompound,
                                    BigDecimal sentimentConfidence, Instant createdAt) {
        return new Mention(id, mentionStreamId, brandId, content, sourcePlatform, sourceUrl,
                sourceReliability, author, publishedAt, category, sentimentPositive,
                sentimentNegative, sentimentNeutral, sentimentCompound, sentimentConfidence, createdAt);
    }

    public Long getId() { return id; }
    public Long getMentionStreamId() { return mentionStreamId; }
    public Long getBrandId() { return brandId; }
    public String getContent() { return content; }
    public String getSourcePlatform() { return sourcePlatform; }
    public String getSourceUrl() { return sourceUrl; }
    public BigDecimal getSourceReliability() { return sourceReliability; }
    public String getAuthor() { return author; }
    public Instant getPublishedAt() { return publishedAt; }
    public String getCategory() { return category; }
    public BigDecimal getSentimentPositive() { return sentimentPositive; }
    public BigDecimal getSentimentNegative() { return sentimentNegative; }
    public BigDecimal getSentimentNeutral() { return sentimentNeutral; }
    public BigDecimal getSentimentCompound() { return sentimentCompound; }
    public BigDecimal getSentimentConfidence() { return sentimentConfidence; }
    public Instant getCreatedAt() { return createdAt; }
}