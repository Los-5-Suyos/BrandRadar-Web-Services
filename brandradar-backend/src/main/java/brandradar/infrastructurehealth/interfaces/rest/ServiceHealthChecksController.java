package brandradar.infrastructurehealth.interfaces.rest;

import brandradar.infrastructurehealth.application.commandservices.ServiceHealthCheckCommandService;
import brandradar.infrastructurehealth.application.queries.GetAllHealthChecksQuery;
import brandradar.infrastructurehealth.application.queryservices.ServiceHealthCheckQueryService;
import brandradar.infrastructurehealth.interfaces.rest.resources.CreateServiceHealthCheckResource;
import brandradar.infrastructurehealth.interfaces.rest.resources.ServiceHealthCheckResource;
import brandradar.infrastructurehealth.interfaces.rest.transform.ServiceHealthCheckAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/health-checks", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Service Health Checks", description = "Infrastructure Health Check endpoints")
public class ServiceHealthChecksController {

    private final ServiceHealthCheckCommandService commandService;
    private final ServiceHealthCheckQueryService queryService;

    public ServiceHealthChecksController(ServiceHealthCheckCommandService commandService,
                                         ServiceHealthCheckQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a service health check")
    @PostMapping
    public ResponseEntity<ServiceHealthCheckResource> createHealthCheck(
            @Valid @RequestBody CreateServiceHealthCheckResource resource) {
        var command = ServiceHealthCheckAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(ServiceHealthCheckAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get all health checks")
    @GetMapping
    public ResponseEntity<List<ServiceHealthCheckResource>> getAllHealthChecks() {
        var checks = queryService.handle(new GetAllHealthChecksQuery());
        var resources = checks.stream().map(ServiceHealthCheckAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get health check by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceHealthCheckResource> getHealthCheckById(@PathVariable Long id) {
        return queryService.findById(id)
                .map(ServiceHealthCheckAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}