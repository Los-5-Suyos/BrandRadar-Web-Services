package brandradar.brandworkspace.domain.model.aggregates;

public class WorkspaceConfig {

    private final Long id;
    private final Long workspaceId;
    private final String companyName;
    private final String industry;
    private final String websiteUrl;
    private final String youtubeUrl;
    private final String facebookUrl;
    private final String twitterUrl;
    private final String tiktokUrl;
    private final String instagramUrl;
    private final String redditUrl;
    private final String googleNewsUrl;
    private final String logoUrl;

    private WorkspaceConfig(Long id, Long workspaceId, String companyName, String industry,
                            String websiteUrl, String youtubeUrl, String facebookUrl, String twitterUrl,
                            String tiktokUrl, String instagramUrl, String redditUrl, String googleNewsUrl,
                            String logoUrl) {
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

    public static WorkspaceConfig create(Long workspaceId, String companyName, String industry,
                                         String websiteUrl, String youtubeUrl, String facebookUrl,
                                         String twitterUrl, String tiktokUrl, String instagramUrl,
                                         String redditUrl, String googleNewsUrl, String logoUrl) {
        return new WorkspaceConfig(null, workspaceId, companyName, industry, websiteUrl, youtubeUrl,
                facebookUrl, twitterUrl, tiktokUrl, instagramUrl, redditUrl, googleNewsUrl, logoUrl);
    }

    public static WorkspaceConfig rehydrate(Long id, Long workspaceId, String companyName, String industry,
                                            String websiteUrl, String youtubeUrl, String facebookUrl,
                                            String twitterUrl, String tiktokUrl, String instagramUrl,
                                            String redditUrl, String googleNewsUrl, String logoUrl) {
        return new WorkspaceConfig(id, workspaceId, companyName, industry, websiteUrl, youtubeUrl,
                facebookUrl, twitterUrl, tiktokUrl, instagramUrl, redditUrl, googleNewsUrl, logoUrl);
    }

    /** Combina esta config existente con los campos nuevos de un PATCH — los campos
     *  que vengan null en el patch conservan el valor anterior (no se borran). */
    public WorkspaceConfig withUpdates(String companyName, String industry, String websiteUrl,
                                       String youtubeUrl, String facebookUrl, String twitterUrl,
                                       String tiktokUrl, String instagramUrl, String redditUrl,
                                       String googleNewsUrl, String logoUrl) {
        return new WorkspaceConfig(
                this.id, this.workspaceId,
                companyName != null ? companyName : this.companyName,
                industry != null ? industry : this.industry,
                websiteUrl != null ? websiteUrl : this.websiteUrl,
                youtubeUrl != null ? youtubeUrl : this.youtubeUrl,
                facebookUrl != null ? facebookUrl : this.facebookUrl,
                twitterUrl != null ? twitterUrl : this.twitterUrl,
                tiktokUrl != null ? tiktokUrl : this.tiktokUrl,
                instagramUrl != null ? instagramUrl : this.instagramUrl,
                redditUrl != null ? redditUrl : this.redditUrl,
                googleNewsUrl != null ? googleNewsUrl : this.googleNewsUrl,
                logoUrl != null ? logoUrl : this.logoUrl
        );
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getCompanyName() { return companyName; }
    public String getIndustry() { return industry; }
    public String getWebsiteUrl() { return websiteUrl; }
    public String getYoutubeUrl() { return youtubeUrl; }
    public String getFacebookUrl() { return facebookUrl; }
    public String getTwitterUrl() { return twitterUrl; }
    public String getTiktokUrl() { return tiktokUrl; }
    public String getInstagramUrl() { return instagramUrl; }
    public String getRedditUrl() { return redditUrl; }
    public String getGoogleNewsUrl() { return googleNewsUrl; }
    public String getLogoUrl() { return logoUrl; }
}