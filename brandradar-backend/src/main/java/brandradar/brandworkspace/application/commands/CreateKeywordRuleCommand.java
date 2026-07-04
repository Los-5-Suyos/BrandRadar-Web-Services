package brandradar.brandworkspace.application.commands;

public record CreateKeywordRuleCommand(Long brandId, String keyword, String matchType) {}