package brandradar.brandworkspace.domain.model.aggregates;

public class KeywordRule {

    private final Long id;
    private final Long brandId;
    private final String keyword;
    private final String matchType;
    private final Double weight;
    private final Boolean isActive;

    private KeywordRule(Long id, Long brandId, String keyword, String matchType,
                        Double weight, Boolean isActive) {
        this.id = id;
        this.brandId = brandId;
        this.keyword = keyword;
        this.matchType = matchType;
        this.weight = weight;
        this.isActive = isActive;
    }

    public static KeywordRule create(Long brandId, String keyword, String matchType) {
        return new KeywordRule(null, brandId, keyword,
                matchType != null ? matchType : "PARTIAL", 1.0, true);
    }

    public static KeywordRule rehydrate(Long id, Long brandId, String keyword, String matchType,
                                        Double weight, Boolean isActive) {
        return new KeywordRule(id, brandId, keyword, matchType, weight, isActive);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public String getKeyword() { return keyword; }
    public String getMatchType() { return matchType; }
    public Double getWeight() { return weight; }
    public Boolean getIsActive() { return isActive; }
}