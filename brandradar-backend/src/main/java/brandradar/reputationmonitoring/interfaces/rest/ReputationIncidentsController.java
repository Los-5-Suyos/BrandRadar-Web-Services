package brandradar.reputationmonitoring.interfaces.rest;

import brandradar.crisisdetection.domain.model.repositories.CrisisAnalysisLogRepository;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAnalysisLog;
import brandradar.reputationmonitoring.interfaces.rest.resources.CrisisAnalysisLogResource;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataKeywordRuleRepository;
import brandradar.crisisdetection.application.services.CrisisResponseEngineService;
import brandradar.crisisdetection.interfaces.rest.resources.CrisisAnalysisResultResource;
import brandradar.reputationmonitoring.application.commandservices.ReputationIncidentCommandService;
import brandradar.reputationmonitoring.application.queries.GetIncidentsByBrandIdQuery;
import brandradar.reputationmonitoring.application.queryservices.ReputationIncidentQueryService;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.reputationmonitoring.interfaces.rest.resources.CreateReputationIncidentResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.IncidentKeywordsResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.ReputationIncidentResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.UpdateIncidentStatusResource;
import brandradar.reputationmonitoring.interfaces.rest.transform.ReputationIncidentAssembler;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/incidents", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Reputation Incidents", description = "Casos con seguimiento: estado, progreso, asignación y resolución")
public class ReputationIncidentsController {

    private final ReputationIncidentCommandService commandService;
    private final ReputationIncidentQueryService queryService;
    private final OwnershipGuard ownershipGuard;
    private final CrisisResponseEngineService crisisResponseEngineService;
    private final BrandRepository brandRepository;
    private final MentionRepository mentionRepository;
    private final SpringDataKeywordRuleRepository keywordRuleRepository;
    private final CrisisAnalysisLogRepository crisisAnalysisLogRepository;

    public ReputationIncidentsController(ReputationIncidentCommandService commandService,
                                         ReputationIncidentQueryService queryService,
                                         OwnershipGuard ownershipGuard,
                                         CrisisResponseEngineService crisisResponseEngineService,
                                         BrandRepository brandRepository,
                                         MentionRepository mentionRepository,
                                         SpringDataKeywordRuleRepository keywordRuleRepository,
                                         CrisisAnalysisLogRepository crisisAnalysisLogRepository) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
        this.crisisResponseEngineService = crisisResponseEngineService;
        this.brandRepository = brandRepository;
        this.mentionRepository = mentionRepository;
        this.keywordRuleRepository = keywordRuleRepository;
        this.crisisAnalysisLogRepository = crisisAnalysisLogRepository;
    }

    @Operation(summary = "Create a reputation incident manually")
    @PostMapping
    public ResponseEntity<ReputationIncidentResource> createIncident(
            @Valid @RequestBody CreateReputationIncidentResource resource) {
        ownershipGuard.assertBrandOwnership(resource.brandId());
        var command = ReputationIncidentAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(ReputationIncidentAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get incidents by brand ID, con tabs opcionales por estado y severidad " +
            "— ej. ?status=ACTIVO o ?status=RESUELTO (historial) o ?severity=CRITICO")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ReputationIncidentResource>> getIncidentsByBrandId(
            @PathVariable Long brandId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity) {
        ownershipGuard.assertBrandOwnership(brandId);
        var incidents = queryService.handle(new GetIncidentsByBrandIdQuery(brandId));

        var filtered = incidents.stream()
                .filter(i -> status == null || status.equalsIgnoreCase(i.getStatus()))
                .filter(i -> severity == null || severity.equalsIgnoreCase(i.getSeverityLabel()))
                .sorted(Comparator.comparing(brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident::getCreatedAt).reversed())
                .toList();

        var resources = filtered.stream().map(ReputationIncidentAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get incident by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReputationIncidentResource> getIncidentById(@PathVariable Long id) {
        var incident = queryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident not found"));
        ownershipGuard.assertBrandOwnership(incident.getBrandId());
        return ResponseEntity.ok(ReputationIncidentAssembler.toResource(incident));
    }

    @Operation(summary = "Update incident status/progress: ACTIVO → MONITOREADO → RESUELTO")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReputationIncidentResource> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateIncidentStatusResource resource) {
        var incident = queryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident not found"));
        ownershipGuard.assertBrandOwnership(incident.getBrandId());

        if (resource.status() != null
                && !List.of("ACTIVO", "MONITOREADO", "RESUELTO").contains(resource.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Status must be ACTIVO, MONITOREADO or RESUELTO");
        }

        var updated = incident.withStatusUpdate(resource.status(), resource.progressPct(), resource.resolutionNotes());
        var saved = commandService.updateStatus(updated);
        return ResponseEntity.ok(ReputationIncidentAssembler.toResource(saved));
    }

    @Operation(summary = "Generate an AI diagnosis for this incident (pattern, keywords, " +
            "geofocus, diagnostico, accion) usando Groq — conecta el motor que ya existía en " +
            "/crisis-engine/analyze, scoped al incidente, y guarda el resultado en el historial")
    @PostMapping("/{id}/analyze")
    public ResponseEntity<CrisisAnalysisResultResource> analyzeIncident(@PathVariable Long id) {
        var incident = queryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident not found"));
        ownershipGuard.assertBrandOwnership(incident.getBrandId());

        var brand = brandRepository.findById(incident.getBrandId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));

        String description = (incident.getTitle() != null ? incident.getTitle() + ". " : "")
                + (incident.getDescription() != null ? incident.getDescription() : "");

        var result = crisisResponseEngineService.analyzeCrisis(brand.getName(), description);

        var log = CrisisAnalysisLog.create(id, result.pattern(), result.keywords(),
                result.geofocus(), result.diagnostico(), result.accion());
        crisisAnalysisLogRepository.save(log);

        return ResponseEntity.ok(new CrisisAnalysisResultResource(
                result.pattern(), result.keywords(), result.geofocus(), result.diagnostico(), result.accion()
        ));
    }

    @Operation(summary = "Get the AI analysis history for this incident, most recent first")
    @GetMapping("/{id}/analysis-history")
    public ResponseEntity<List<CrisisAnalysisLogResource>> getAnalysisHistory(@PathVariable Long id) {
        var incident = queryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident not found"));
        ownershipGuard.assertBrandOwnership(incident.getBrandId());

        var history = crisisAnalysisLogRepository.findByIncidentIdOrderByCreatedAtDesc(id).stream()
                .map(l -> new CrisisAnalysisLogResource(l.getId(), l.getPattern(), l.getKeywords(),
                        l.getGeofocus(), l.getDiagnostico(), l.getAccion(), l.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Get keyword ranking scoped to this incident's mention stream " +
            "(mismo cálculo que el dashboard, pero acotado a las menciones de este incidente)")
    @GetMapping("/{id}/keywords")
    public ResponseEntity<IncidentKeywordsResource> getIncidentKeywords(@PathVariable Long id) {
        var incident = queryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident not found"));
        ownershipGuard.assertBrandOwnership(incident.getBrandId());

        var negativeMentions = (incident.getMentionStreamId() != null
                ? mentionRepository.findByMentionStreamId(incident.getMentionStreamId())
                : mentionRepository.findByBrandId(incident.getBrandId())
        ).stream().filter(m -> m.getSentimentCompound().doubleValue() < -0.3).toList();

        var inclusionKeywords = keywordRuleRepository.findByBrandId(incident.getBrandId());

        var counts = inclusionKeywords.stream()
                .collect(Collectors.toMap(
                        k -> k.getKeyword(),
                        k -> negativeMentions.stream()
                                .filter(m -> m.getContent() != null &&
                                        m.getContent().toLowerCase(Locale.ROOT)
                                                .contains(k.getKeyword().toLowerCase(Locale.ROOT)))
                                .count(),
                        (a, b) -> a
                ));

        long max = counts.values().stream().mapToLong(Long::longValue).max().orElse(1);

        var keywordCounts = counts.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> new IncidentKeywordsResource.KeywordCount(
                        e.getKey(), e.getValue(), Math.round((double) e.getValue() / max * 1000.0) / 10.0))
                .toList();

        return ResponseEntity.ok(new IncidentKeywordsResource(id, keywordCounts));
    }
}