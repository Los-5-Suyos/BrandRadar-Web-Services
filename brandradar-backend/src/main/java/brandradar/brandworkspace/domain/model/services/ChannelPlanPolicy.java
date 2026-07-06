package brandradar.brandworkspace.domain.model.services;

import java.util.Map;
import java.util.Set;

 
public class ChannelPlanPolicy {

    private ChannelPlanPolicy() {}

    public static final Map<String, Set<String>> CHANNELS_BY_PLAN = Map.of(
            "FREE", Set.of("YOUTUBE", "TWITTER", "REDDIT", "TIKTOK"),
            "PRO", Set.of("YOUTUBE", "FACEBOOK", "TWITTER", "TIKTOK", "INSTAGRAM",
                    "GOOGLE_NEWS", "REDDIT", "BLOGS"),
            "ENTERPRISE", Set.of("YOUTUBE", "FACEBOOK", "TWITTER", "TIKTOK", "INSTAGRAM",
                    "GOOGLE_NEWS", "REDDIT", "BLOGS")
    );

    public static final java.util.List<String> ALL_CHANNELS = java.util.List.of(
            "YOUTUBE", "TWITTER", "REDDIT", "TIKTOK", "FACEBOOK", "INSTAGRAM", "GOOGLE_NEWS", "BLOGS"
    );

    public static Set<String> allowedFor(String plan) {
        return CHANNELS_BY_PLAN.getOrDefault(plan, Set.of("YOUTUBE"));
    }
}