package brandradar.infrastructurehealth.interfaces.rest;

import brandradar.infrastructurehealth.application.commandservices.InfraIncidentCommandService;
import brandradar.infrastructurehealth.application.queries.GetInfraIncidentsByStatusQuery;
import brandradar.infrastructurehealth.application.queryservices.InfraIncidentQueryService;
import brandradar.infrastructurehealth.interfaces.rest.resources.CreateInfraIncidentResource;
import brandradar.infrastructurehealth.interfaces.rest.resources.InfraIncidentResource;
import brandradar.infrastructurehealth.interfaces.rest.transform.InfraIncidentAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/infra-incidents", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Infrastructure Incidents", description = "Infrastructure Incident management endpoints")
public class InfraIncidentsController {

    private final InfraIncidentCommandService commandService;
    private final InfraIncidentQueryService queryService;

    public InfraIncidentsController(InfraIncidentCommandService commandService, InfraIncidentQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create an infrastructure incident")
    @PostMapping
    public ResponseEntity<InfraIncidentResource> createInfraIncident(
            @Valid @RequestBody CreateInfraIncidentResource resource) {
        var command = InfraIncidentAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(InfraIncidentAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get infrastructure incidents by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InfraIncidentResource>> getInfraIncidentsByStatus(
            @PathVariable String status) {
        var incidents = queryService.handle(new GetInfraIncidentsByStatusQuery(status));
        var resources = incidents.stream().map(InfraIncidentAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get infrastructure incident by ID")
    @GetMapping("/{id}")
    public ResponseEntity<InfraIncidentResource> getInfraIncidentById(@PathVariable Long id) {
        return queryService.findById(id)
                .map(InfraIncidentAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}