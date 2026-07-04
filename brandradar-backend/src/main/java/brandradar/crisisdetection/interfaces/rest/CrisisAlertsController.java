package brandradar.crisisdetection.interfaces.rest;

import brandradar.crisisdetection.application.commandservices.CrisisAlertCommandService;
import brandradar.crisisdetection.application.queries.GetCrisisAlertsByBrandIdQuery;
import brandradar.crisisdetection.application.queryservices.CrisisAlertQueryService;
import brandradar.crisisdetection.interfaces.rest.resources.CreateCrisisAlertResource;
import brandradar.crisisdetection.interfaces.rest.resources.CrisisAlertResource;
import brandradar.crisisdetection.interfaces.rest.transform.CrisisAlertAssembler;
import brandradar.shared.infrastructure.security.OwnershipGuard;
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
@RequestMapping(value = "/api/v1/crisis-alerts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Crisis Alerts", description = "Crisis Alert management endpoints")
public class CrisisAlertsController {

    private final CrisisAlertCommandService commandService;
    private final CrisisAlertQueryService queryService;
    private final OwnershipGuard ownershipGuard;

    public CrisisAlertsController(CrisisAlertCommandService commandService,
                                  CrisisAlertQueryService queryService,
                                  OwnershipGuard ownershipGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
    }

    @Operation(summary = "Create a crisis alert")
    @PostMapping
    public ResponseEntity<CrisisAlertResource> createCrisisAlert(
            @Valid @RequestBody CreateCrisisAlertResource resource) {
        ownershipGuard.assertBrandOwnership(resource.brandId());
        var command = CrisisAlertAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(CrisisAlertAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get crisis alerts by brand ID")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<CrisisAlertResource>> getCrisisAlertsByBrandId(
            @PathVariable Long brandId) {
        ownershipGuard.assertBrandOwnership(brandId);
        var alerts = queryService.handle(new GetCrisisAlertsByBrandIdQuery(brandId));
        var resources = alerts.stream().map(CrisisAlertAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get crisis alert by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CrisisAlertResource> getCrisisAlertById(@PathVariable Long id) {
        var alert = queryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crisis alert not found"));
        ownershipGuard.assertBrandOwnership(alert.getBrandId());
        return ResponseEntity.ok(CrisisAlertAssembler.toResource(alert));
    }
}