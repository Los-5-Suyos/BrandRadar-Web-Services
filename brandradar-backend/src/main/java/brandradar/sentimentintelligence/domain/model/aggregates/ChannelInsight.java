package brandradar.sentimentintelligence.domain.model.aggregates;

public class ChannelInsight {

    private final Long id;
    private final Long brandId;
    private final String channelType;
    private final String insightText;

    private ChannelInsight(Long id, Long brandId, String channelType, String insightText) {
        this.id = id;
        this.brandId = brandId;
        this.channelType = channelType;
        this.insightText = insightText;
    }

    public static ChannelInsight create(Long brandId, String channelType, String insightText) {
        return new ChannelInsight(null, brandId, channelType, insightText);
    }

    public static ChannelInsight rehydrate(Long id, Long brandId, String channelType, String insightText) {
        return new ChannelInsight(id, brandId, channelType, insightText);
    }

    public ChannelInsight withText(String insightText) {
        return new ChannelInsight(this.id, this.brandId, this.channelType, insightText);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public String getChannelType() { return channelType; }
    public String getInsightText() { return insightText; }
}