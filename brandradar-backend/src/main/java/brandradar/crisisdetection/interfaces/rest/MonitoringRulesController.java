package brandradar.crisisdetection.interfaces.rest;

import brandradar.crisisdetection.application.commandservices.MonitoringRuleCommandService;
import brandradar.crisisdetection.application.queries.GetMonitoringRulesByBrandIdQuery;
import brandradar.crisisdetection.application.queryservices.MonitoringRuleQueryService;
import brandradar.crisisdetection.interfaces.rest.resources.CreateMonitoringRuleResource;
import brandradar.crisisdetection.interfaces.rest.resources.MonitoringRuleResource;
import brandradar.crisisdetection.interfaces.rest.transform.MonitoringRuleAssembler;
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
@RequestMapping(value = "/api/v1/monitoring-rules", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Monitoring Rules", description = "Monitoring Rule management endpoints")
public class MonitoringRulesController {

    private final MonitoringRuleCommandService commandService;
    private final MonitoringRuleQueryService queryService;
    private final OwnershipGuard ownershipGuard;

    public MonitoringRulesController(MonitoringRuleCommandService commandService,
                                     MonitoringRuleQueryService queryService,
                                     OwnershipGuard ownershipGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
    }

    @Operation(summary = "Create a monitoring rule")
    @PostMapping
    public ResponseEntity<MonitoringRuleResource> createMonitoringRule(
            @Valid @RequestBody CreateMonitoringRuleResource resource) {
        ownershipGuard.assertBrandOwnership(resource.brandId());
        var command = MonitoringRuleAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(MonitoringRuleAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get monitoring rules by brand ID")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<MonitoringRuleResource>> getMonitoringRulesByBrandId(
            @PathVariable Long brandId) {
        ownershipGuard.assertBrandOwnership(brandId);
        var rules = queryService.handle(new GetMonitoringRulesByBrandIdQuery(brandId));
        var resources = rules.stream().map(MonitoringRuleAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get monitoring rule by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MonitoringRuleResource> getMonitoringRuleById(@PathVariable Long id) {
        var rule = queryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Monitoring rule not found"));
        ownershipGuard.assertBrandOwnership(rule.getBrandId());
        return ResponseEntity.ok(MonitoringRuleAssembler.toResource(rule));
    }

    @Operation(summary = "Delete monitoring rule by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringRule(@PathVariable Long id) {
        var rule = queryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Monitoring rule not found"));
        ownershipGuard.assertBrandOwnership(rule.getBrandId());
        commandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}