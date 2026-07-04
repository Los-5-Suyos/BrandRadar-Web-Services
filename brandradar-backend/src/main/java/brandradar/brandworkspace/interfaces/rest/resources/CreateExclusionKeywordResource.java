package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateExclusionKeywordResource(
        @NotBlank String keyword
) {}