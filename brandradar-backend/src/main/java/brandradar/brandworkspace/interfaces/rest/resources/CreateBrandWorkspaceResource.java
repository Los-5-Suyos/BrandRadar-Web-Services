package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBrandWorkspaceResource(
        @NotNull Long userId,
        @NotBlank String name,
        String description
) {}
