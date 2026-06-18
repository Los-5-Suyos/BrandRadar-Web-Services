package brandradar.crisisdetection.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CrisisAnalysisResource(
        @NotBlank String brandName,
        @NotBlank String crisisDescription
) {}