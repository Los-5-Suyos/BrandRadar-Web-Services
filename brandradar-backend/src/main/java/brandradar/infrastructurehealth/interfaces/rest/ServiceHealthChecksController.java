package brandradar.infrastructurehealth.interfaces.rest;

import brandradar.infrastructurehealth.application.commandservices.ServiceHealthCheckCommandService;
import brandradar.infrastructurehealth.application.queries.GetAllHealthChecksQuery;
import brandradar.infrastructurehealth.application.queryservices.ServiceHealthCheckQueryService;
import brandradar.infrastructurehealth.interfaces.rest.resources.CreateServiceHealthCheckResource;
import brandradar.infrastructurehealth.interfaces.rest.resources.ServiceHealthCheckResource;
import brandradar.infrastructurehealth.interfaces.rest.transform.ServiceHealthCheckAssembler;
import brandradar.shared.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/health-checks", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Service Health Checks", description = "Infrastructure Health Check endpoints (ADMIN only — " +
        "uptime y estado de las fuentes de datos externas: YouTube, SociaVault, Groq)")
public class ServiceHealthChecksController {

    private final ServiceHealthCheckCommandService commandService;
    private final ServiceHealthCheckQueryService queryService;
    private final CurrentUser currentUser;

    public ServiceHealthChecksController(ServiceHealthCheckCommandService commandService,
                                         ServiceHealthCheckQueryService queryService,
                                         CurrentUser currentUser) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Create a service health check (ADMIN only)")
    @PostMapping
    public ResponseEntity<ServiceHealthCheckResource> createHealthCheck(
            @Valid @RequestBody CreateServiceHealthCheckResource resource) {
        assertAdmin();
        var command = ServiceHealthCheckAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(ServiceHealthCheckAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get all health checks (ADMIN only)")
    @GetMapping
    public ResponseEntity<List<ServiceHealthCheckResource>> getAllHealthChecks() {
        assertAdmin();
        var checks = queryService.handle(new GetAllHealthChecksQuery());
        var resources = checks.stream().map(ServiceHealthCheckAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get health check by ID (ADMIN only)")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceHealthCheckResource> getHealthCheckById(@PathVariable Long id) {
        assertAdmin();
        return queryService.findById(id)
                .map(ServiceHealthCheckAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private void assertAdmin() {
        if (!"ADMIN".equals(currentUser.get().role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }
}