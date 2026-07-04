package brandradar.brandworkspace.interfaces.rest.resources;

public record KeywordRuleResource(
        Long id,
        Long brandId,
        String keyword,
        String matchType,
        Double weight,
        Boolean isActive
) {}