package brandradar.reputationmonitoring.interfaces.rest;

import brandradar.reputationmonitoring.application.commandservices.ReputationIncidentCommandService;
import brandradar.reputationmonitoring.application.queries.GetIncidentsByBrandIdQuery;
import brandradar.reputationmonitoring.application.queryservices.ReputationIncidentQueryService;
import brandradar.reputationmonitoring.interfaces.rest.resources.CreateReputationIncidentResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.ReputationIncidentResource;
import brandradar.reputationmonitoring.interfaces.rest.transform.ReputationIncidentAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/incidents", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Reputation Incidents", description = "Reputation Incident management endpoints")
public class ReputationIncidentsController {

    private final ReputationIncidentCommandService commandService;
    private final ReputationIncidentQueryService queryService;

    public ReputationIncidentsController(ReputationIncidentCommandService commandService,
                                         ReputationIncidentQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a reputation incident")
    @PostMapping
    public ResponseEntity<ReputationIncidentResource> createIncident(
            @Valid @RequestBody CreateReputationIncidentResource resource) {
        var command = ReputationIncidentAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(ReputationIncidentAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get incidents by brand ID")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ReputationIncidentResource>> getIncidentsByBrandId(
            @PathVariable Long brandId) {
        var incidents = queryService.handle(new GetIncidentsByBrandIdQuery(brandId));
        var resources = incidents.stream().map(ReputationIncidentAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get incident by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReputationIncidentResource> getIncidentById(@PathVariable Long id) {
        return queryService.findById(id)
                .map(ReputationIncidentAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}