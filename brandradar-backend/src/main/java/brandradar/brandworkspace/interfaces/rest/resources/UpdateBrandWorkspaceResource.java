package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record UpdateBrandWorkspaceResource(
        @NotBlank String name,
        String description
) {}
