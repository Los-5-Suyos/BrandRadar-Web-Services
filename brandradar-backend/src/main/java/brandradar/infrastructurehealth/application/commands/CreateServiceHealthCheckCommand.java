package brandradar.infrastructurehealth.application.commands;

public record CreateServiceHealthCheckCommand(
        String serviceName,
        String endpointUrl,
        String endpointMethod,
        Integer endpointTimeoutMs
) {}