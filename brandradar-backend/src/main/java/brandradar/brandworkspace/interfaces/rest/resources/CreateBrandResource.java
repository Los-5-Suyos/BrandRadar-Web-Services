package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBrandResource(
        @NotNull Long workspaceId,
        @NotBlank String name
) {}