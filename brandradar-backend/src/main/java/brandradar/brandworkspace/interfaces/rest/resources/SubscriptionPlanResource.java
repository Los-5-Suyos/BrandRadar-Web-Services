package brandradar.brandworkspace.interfaces.rest.resources;

import java.util.List;

public record SubscriptionPlanResource(
        String key,
        String label,
        double priceMonthly,
        double priceYearly,
        List<String> channelsIncluded,
        int maxWorkspaces,
        List<String> features
) {}