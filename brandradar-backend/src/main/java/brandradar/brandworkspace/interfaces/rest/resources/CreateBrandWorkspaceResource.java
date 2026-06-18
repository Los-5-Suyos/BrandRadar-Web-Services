package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateBrandWorkspaceResource(
        @NotBlank String name,
        String description
) {}
