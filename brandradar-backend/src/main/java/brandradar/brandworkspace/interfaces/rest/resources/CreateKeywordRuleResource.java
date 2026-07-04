package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateKeywordRuleResource(
        @NotBlank String keyword,
        String matchType
) {}