package brandradar.reputationmonitoring.domain.model.aggregates;

import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentLabel;
import brandradar.reputationmonitoring.domain.model.valueobjects.SourceType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Mention {

    private final Long id;
    private final Long workspaceId;
    private final SourceType source;
    private final String authorName;
    private final Integer authorFollowers;
    private final String content;
    private final String url;
    private final BigDecimal sentimentScore;
    private final SentimentLabel sentimentLabel;
    private final Instant publishedAt;
    private final Instant createdAt;
    private final boolean isActive;

    private Mention(Long id, Long workspaceId, SourceType source, String authorName, Integer authorFollowers,
                    String content, String url, BigDecimal sentimentScore, SentimentLabel sentimentLabel,
                    Instant publishedAt, Instant createdAt, boolean isActive) {
        this.id = id;
        this.workspaceId = Objects.requireNonNull(workspaceId, "WorkspaceId is required");
        this.source = Objects.requireNonNull(source, "Source is required");
        this.authorName = authorName;
        this.authorFollowers = authorFollowers;
        this.content = Objects.requireNonNull(content, "Content is required");
        this.url = url;
        this.sentimentScore = Objects.requireNonNull(sentimentScore, "SentimentScore is required");
        this.sentimentLabel = Objects.requireNonNull(sentimentLabel, "SentimentLabel is required");
        this.publishedAt = Objects.requireNonNull(publishedAt, "PublishedAt is required");
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public static Mention create(Long workspaceId, SourceType source, String authorName, Integer authorFollowers,
                                  String content, String url, BigDecimal sentimentScore,
                                  SentimentLabel sentimentLabel, Instant publishedAt) {
        return new Mention(null, workspaceId, source, authorName, authorFollowers, content, url,
                sentimentScore, sentimentLabel, publishedAt, null, true);
    }

    public static Mention rehydrate(Long id, Long workspaceId, SourceType source, String authorName,
                                    Integer authorFollowers, String content, String url, BigDecimal sentimentScore,
                                    SentimentLabel sentimentLabel, Instant publishedAt, Instant createdAt,
                                    boolean isActive) {
        return new Mention(id, workspaceId, source, authorName, authorFollowers, content, url,
                sentimentScore, sentimentLabel, publishedAt, createdAt, isActive);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public SourceType getSource() { return source; }
    public String getAuthorName() { return authorName; }
    public Integer getAuthorFollowers() { return authorFollowers; }
    public String getContent() { return content; }
    public String getUrl() { return url; }
    public BigDecimal getSentimentScore() { return sentimentScore; }
    public SentimentLabel getSentimentLabel() { return sentimentLabel; }
    public Instant getPublishedAt() { return publishedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isActive() { return isActive; }
}
