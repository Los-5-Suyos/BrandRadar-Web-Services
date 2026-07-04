package brandradar.reputationmonitoring.interfaces.rest;

import brandradar.reputationmonitoring.application.services.MentionExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import brandradar.crisisdetection.infrastructure.groq.GroqApiClient;
import brandradar.reputationmonitoring.application.commandservices.MentionCommandService;
import brandradar.reputationmonitoring.application.queries.GetMentionsByBrandIdQuery;
import brandradar.reputationmonitoring.application.queryservices.MentionQueryService;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.reputationmonitoring.infrastructure.providers.SociaVaultTikTokProvider;
import brandradar.reputationmonitoring.interfaces.rest.resources.ChannelCountResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.CreateMentionResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.MentionAiAnalysisResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.MentionResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.TikTokCommentsResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.UpdateMentionStatusResource;
import brandradar.reputationmonitoring.interfaces.rest.transform.MentionAssembler;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/mentions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Mentions", description = "Mention management endpoints")
public class MentionsController {

    private static final String AI_ANALYSIS_SYSTEM_PROMPT = """
            Eres un experto en gestión de reputación de marcas y atención al cliente en redes sociales.
            Dada una mención (comentario/post) sobre una marca, responde SIEMPRE en JSON válido, sin texto
            adicional ni markdown, con exactamente estos 4 campos de tipo string:
            {
              "analisis": "diagnóstico breve del sentimiento y la causa raíz del comentario",
              "estrategia": "estrategia recomendada de respuesta (tono, prioridad, canal)",
              "borrador": "un borrador de respuesta pública lista para publicar, en español, cordial y profesional",
              "accion": "próxima acción interna recomendada (ej. escalar a soporte, ofrecer compensación, monitorear)"
            }
            """;

    private final MentionCommandService commandService;
    private final MentionQueryService queryService;
    private final MentionRepository mentionRepository;
    private final GroqApiClient groqApiClient;
    private final OwnershipGuard ownershipGuard;
    private final SociaVaultTikTokProvider tikTokProvider;
    private final CrisisAlertRepository crisisAlertRepository;
    private final MentionExportService exportService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MentionsController(MentionCommandService commandService,
                              MentionQueryService queryService,
                              MentionRepository mentionRepository,
                              GroqApiClient groqApiClient,
                              OwnershipGuard ownershipGuard,
                              SociaVaultTikTokProvider tikTokProvider,
                              CrisisAlertRepository crisisAlertRepository,
                              MentionExportService exportService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.mentionRepository = mentionRepository;
        this.groqApiClient = groqApiClient;
        this.ownershipGuard = ownershipGuard;
        this.tikTokProvider = tikTokProvider;
        this.crisisAlertRepository = crisisAlertRepository;
        this.exportService = exportService;
    }

    @Operation(summary = "Create a mention")
    @PostMapping
    public ResponseEntity<MentionResource> createMention(
            @Valid @RequestBody CreateMentionResource resource) {
        ownershipGuard.assertBrandOwnership(resource.brandId());
        var command = MentionAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(MentionAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get mentions by brand ID, con filtros opcionales de sentimiento, " +
            "plataforma y búsqueda de texto — ej. ?sentiment=NEGATIVE&platform=TWITTER&search=demora")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<MentionResource>> getMentionsByBrandId(
            @PathVariable Long brandId,
            @RequestParam(required = false) String sentiment,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String search) {
        ownershipGuard.assertBrandOwnership(brandId);
        var mentions = queryService.handle(new GetMentionsByBrandIdQuery(brandId));

        var filtered = mentions.stream()
                .filter(m -> sentiment == null || matchesSentiment(m.getSentimentCompound(), sentiment))
                .filter(m -> platform == null || platform.equalsIgnoreCase(m.getSourcePlatform()))
                .filter(m -> search == null || (m.getContent() != null
                        && m.getContent().toLowerCase(Locale.ROOT)
                        .contains(search.toLowerCase(Locale.ROOT))))
                .toList();

        var resources = filtered.stream().map(MentionAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get mention counts grouped by channel/platform for a brand")
    @GetMapping("/brand/{brandId}/channel-counts")
    public ResponseEntity<List<ChannelCountResource>> getChannelCounts(@PathVariable Long brandId) {
        ownershipGuard.assertBrandOwnership(brandId);
        var mentions = queryService.handle(new GetMentionsByBrandIdQuery(brandId));

        var counts = mentions.stream()
                .filter(m -> m.getSourcePlatform() != null)
                .collect(Collectors.groupingBy(Mention::getSourcePlatform, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new ChannelCountResource(e.getKey(), e.getValue()))
                .sorted((a, b) -> Long.compare(b.count(), a.count()))
                .toList();

        return ResponseEntity.ok(counts);
    }

    @Operation(summary = "Export mentions of a brand as CSV, Excel or PDF")
    @GetMapping("/brand/{brandId}/export")
    public ResponseEntity<byte[]> exportMentions(
            @PathVariable Long brandId,
            @RequestParam(defaultValue = "csv") String format) {
        ownershipGuard.assertBrandOwnership(brandId);
        var mentions = queryService.handle(new GetMentionsByBrandIdQuery(brandId));

        byte[] fileContent;
        String filename;
        MediaType mediaType;

        switch (format.toLowerCase(Locale.ROOT)) {
            case "excel", "xlsx" -> {
                fileContent = exportService.toExcel(mentions);
                filename = "mentions-" + brandId + ".xlsx";
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }
            case "pdf" -> {
                fileContent = exportService.toPdf(mentions, "Brand " + brandId);
                filename = "mentions-" + brandId + ".pdf";
                mediaType = MediaType.APPLICATION_PDF;
            }
            case "csv" -> {
                fileContent = exportService.toCsv(mentions);
                filename = "mentions-" + brandId + ".csv";
                mediaType = MediaType.parseMediaType("text/csv");
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported format: " + format + ". Use csv, excel or pdf");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(mediaType)
                .body(fileContent);
    }

    @Operation(summary = "Generate an AI analysis + response draft for a single mention " +
            "(replaces the direct Groq call that used to live in the frontend, see 0.1)")
    @PostMapping("/{mentionId}/ai-analysis")
    public ResponseEntity<MentionAiAnalysisResource> generateAiAnalysis(@PathVariable Long mentionId) {
        var mention = mentionRepository.findById(mentionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mention not found"));
        ownershipGuard.assertBrandOwnership(mention.getBrandId());

        var prompt = String.format("""
                Plataforma: %s
                Autor: %s
                Contenido: %s
                Sentimiento (compound, -1 a 1): %s
                """,
                mention.getSourcePlatform(),
                mention.getAuthor(),
                mention.getContent(),
                mention.getSentimentCompound());

        try {
            var raw = groqApiClient.chat(AI_ANALYSIS_SYSTEM_PROMPT, prompt);
            var json = objectMapper.readTree(stripMarkdownFences(raw));
            var analysis = new MentionAiAnalysisResource(
                    json.path("analisis").asText(""),
                    json.path("estrategia").asText(""),
                    json.path("borrador").asText(""),
                    json.path("accion").asText("")
            );
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            log.error("Error generating AI analysis for mention {}: {}", mentionId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Could not generate AI analysis right now");
        }
    }

    @Operation(summary = "Mark a mention as ATENDIDA or back to PENDIENTE")
    @PatchMapping("/{mentionId}/status")
    public ResponseEntity<MentionResource> updateStatus(
            @PathVariable Long mentionId,
            @Valid @RequestBody UpdateMentionStatusResource resource) {
        var mention = mentionRepository.findById(mentionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mention not found"));
        ownershipGuard.assertBrandOwnership(mention.getBrandId());

        if (!"ATENDIDA".equals(resource.status()) && !"PENDIENTE".equals(resource.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status must be ATENDIDA or PENDIENTE");
        }

        var updated = mentionRepository.save(mention.withStatus(resource.status()));
        return ResponseEntity.ok(MentionAssembler.toResource(updated));
    }

    @Operation(summary = "Create a CrisisAlert manually from a specific mention — used by the " +
            "'Crear Incidente' button in the Mentions screen")
    @PostMapping("/{mentionId}/create-incident")
    public ResponseEntity<Long> createIncidentFromMention(@PathVariable Long mentionId) {
        var mention = mentionRepository.findById(mentionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mention not found"));
        ownershipGuard.assertBrandOwnership(mention.getBrandId());

        String snippet = mention.getContent().length() > 100
                ? mention.getContent().substring(0, 100) + "..."
                : mention.getContent();

        var alert = CrisisAlert.create(
                mention.getBrandId(),
                mention.getMentionStreamId(),
                null,
                2,
                "ALTO",
                "Incidente creado manualmente desde una mención",
                "Origen: " + mention.getSourcePlatform() + " — " + mention.getAuthor() + ": \"" + snippet + "\"",
                "MANUAL",
                BigDecimal.ZERO,
                BigDecimal.ONE
        );
        var saved = crisisAlertRepository.save(alert);

        mentionRepository.save(mention.withStatus("ATENDIDA"));

        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }

    @Operation(summary = "Fetch real TikTok comments for a specific mention (drill-down bajo " +
            "demanda, gasta 1 crédito de SociaVault por llamada — solo cuando el usuario elige " +
            "ver los comentarios de un video puntual)")
    @GetMapping("/{mentionId}/tiktok-comments")
    public ResponseEntity<TikTokCommentsResource> getTikTokComments(@PathVariable Long mentionId) {
        var mention = mentionRepository.findById(mentionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mention not found"));
        ownershipGuard.assertBrandOwnership(mention.getBrandId());

        if (!"TIKTOK".equals(mention.getSourcePlatform()) || mention.getSourceUrl() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This mention is not a TikTok video with a valid URL");
        }

        var result = tikTokProvider.fetchComments(mention.getSourceUrl());
        var comments = result.comments().stream()
                .map(c -> new TikTokCommentsResource.CommentItem(
                        c.text(), c.authorName(), c.authorHandle(), c.likes(), c.publishedAt()))
                .toList();

        return ResponseEntity.ok(new TikTokCommentsResource(result.total(), comments));
    }

    private boolean matchesSentiment(BigDecimal compound, String sentimentFilter) {
        if (compound == null) return false;
        double value = compound.doubleValue();
        return switch (sentimentFilter.toUpperCase(Locale.ROOT)) {
            case "POSITIVE", "POSITIVO" -> value > 0.3;
            case "NEGATIVE", "NEGATIVO" -> value < -0.3;
            case "NEUTRAL" -> value >= -0.3 && value <= 0.3;
            default -> true;
        };
    }

    private String stripMarkdownFences(String text) {
        var trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.replaceFirst("^```(json)?", "").trim();
            if (trimmed.endsWith("```")) {
                trimmed = trimmed.substring(0, trimmed.length() - 3).trim();
            }
        }
        return trimmed;
    }
}