package brandradar.brandworkspace.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "WorkspaceConfig")
public class WorkspaceConfigJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WCF_id")
    private Long id;

    @Column(name = "BWS_id", nullable = false, unique = true)
    private Long workspaceId;

    @Column(name = "WCF_company_name", length = 255)
    private String companyName;

    @Column(name = "WCF_industry", length = 100)
    private String industry;

    @Column(name = "WCF_website_url", length = 500)
    private String websiteUrl;

    @Column(name = "WCF_youtube_url", length = 500)
    private String youtubeUrl;

    @Column(name = "WCF_facebook_url", length = 500)
    private String facebookUrl;

    @Column(name = "WCF_twitter_url", length = 500)
    private String twitterUrl;

    @Column(name = "WCF_tiktok_url", length = 500)
    private String tiktokUrl;

    @Column(name = "WCF_instagram_url", length = 500)
    private String instagramUrl;

    @Column(name = "WCF_reddit_url", length = 500)
    private String redditUrl;

    @Column(name = "WCF_google_news_url", length = 500)
    private String googleNewsUrl;

    @Column(name = "WCF_logo_url", length = 500)
    private String logoUrl;

    @CreatedDate
    @Column(name = "WCF_created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "WCF_updated_at", nullable = false)
    private Instant updatedAt;

    public WorkspaceConfigJpaEntity(Long id, Long workspaceId, String companyName, String industry, String websiteUrl, String youtubeUrl, String facebookUrl, String twitterUrl, String tiktokUrl, String instagramUrl, String redditUrl, String googleNewsUrl, String logoUrl) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.companyName = companyName;
        this.industry = industry;
        this.websiteUrl = websiteUrl;
        this.youtubeUrl = youtubeUrl;
        this.facebookUrl = facebookUrl;
        this.twitterUrl = twitterUrl;
        this.tiktokUrl = tiktokUrl;
        this.instagramUrl = instagramUrl;
        this.redditUrl = redditUrl;
        this.googleNewsUrl = googleNewsUrl;
        this.logoUrl = logoUrl;
    }
}