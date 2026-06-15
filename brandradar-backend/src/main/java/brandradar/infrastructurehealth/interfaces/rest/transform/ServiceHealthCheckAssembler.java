package brandradar.infrastructurehealth.interfaces.rest.transform;

import brandradar.infrastructurehealth.application.commands.CreateServiceHealthCheckCommand;
import brandradar.infrastructurehealth.domain.model.aggregates.ServiceHealthCheck;
import brandradar.infrastructurehealth.interfaces.rest.resources.CreateServiceHealthCheckResource;
import brandradar.infrastructurehealth.interfaces.rest.resources.ServiceHealthCheckResource;

public class ServiceHealthCheckAssembler {

    private ServiceHealthCheckAssembler() {}

    public static CreateServiceHealthCheckCommand toCommand(CreateServiceHealthCheckResource resource) {
        return new CreateServiceHealthCheckCommand(
                resource.serviceName(),
                resource.endpointUrl(),
                resource.endpointMethod(),
                resource.endpointTimeoutMs()
        );
    }

    public static ServiceHealthCheckResource toResource(ServiceHealthCheck healthCheck) {
        return new ServiceHealthCheckResource(
                healthCheck.getId(),
                healthCheck.getServiceName(),
                healthCheck.getEndpointUrl(),
                healthCheck.getEndpointMethod(),
                healthCheck.getEndpointTimeoutMs(),
                healthCheck.getStatus(),
                healthCheck.getUptimeTotalChecks(),
                healthCheck.getUptimeSuccessfulChecks(),
                healthCheck.getUptimeWindowDays(),
                healthCheck.getLastCheckedAt(),
                healthCheck.getCreatedAt(),
                healthCheck.getUpdatedAt()
        );
    }
}