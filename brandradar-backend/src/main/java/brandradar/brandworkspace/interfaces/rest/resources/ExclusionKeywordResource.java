package brandradar.brandworkspace.interfaces.rest.resources;

public record ExclusionKeywordResource(
        Long id,
        Long workspaceId,
        String keyword
) {}