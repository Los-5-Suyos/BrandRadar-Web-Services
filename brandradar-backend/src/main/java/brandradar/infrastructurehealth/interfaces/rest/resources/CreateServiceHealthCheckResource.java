package brandradar.infrastructurehealth.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateServiceHealthCheckResource(
        @NotBlank String serviceName,
        @NotBlank String endpointUrl,
        String endpointMethod,
        Integer endpointTimeoutMs
) {}