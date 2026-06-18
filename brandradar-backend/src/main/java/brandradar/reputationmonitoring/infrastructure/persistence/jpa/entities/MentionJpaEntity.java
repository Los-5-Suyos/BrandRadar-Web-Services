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

    @Column(name = "BWS_id", nullable = false)
    private Long workspaceId;

    @Column(name = "MEN_source", nullable = false, length = 20)
    private String source;

    @Column(name = "MEN_author_name", length = 255)
    private String authorName;

    @Column(name = "MEN_author_followers")
    private Integer authorFollowers;

    @Column(name = "MEN_content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "MEN_url", length = 2000)
    private String url;

    @Column(name = "MEN_sentiment_score", nullable = false, precision = 4, scale = 3)
    private BigDecimal sentimentScore;

    @Column(name = "MEN_sentiment_label", nullable = false, length = 10)
    private String sentimentLabel;

    @Column(name = "MEN_published_at", nullable = false)
    private Instant publishedAt;

    @CreatedDate
    @Column(name = "MEN_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "MEN_is_active", nullable = false)
    private boolean isActive;

    public MentionJpaEntity(Long id, Long workspaceId, String source, String authorName, Integer authorFollowers,
                            String content, String url, BigDecimal sentimentScore, String sentimentLabel,
                            Instant publishedAt, boolean isActive) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.source = source;
        this.authorName = authorName;
        this.authorFollowers = authorFollowers;
        this.content = content;
        this.url = url;
        this.sentimentScore = sentimentScore;
        this.sentimentLabel = sentimentLabel;
        this.publishedAt = publishedAt;
        this.isActive = isActive;
    }
}
