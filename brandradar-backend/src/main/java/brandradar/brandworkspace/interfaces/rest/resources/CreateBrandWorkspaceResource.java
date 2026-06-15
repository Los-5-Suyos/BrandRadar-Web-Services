package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateBrandWorkspaceResource(
        @NotNull Long userId,
        @NotBlank String name,
        @Pattern(regexp = "FREE|PRO|ENTERPRISE") String plan
) {}