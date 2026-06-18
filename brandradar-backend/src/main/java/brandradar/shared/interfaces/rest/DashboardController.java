package brandradar.shared.interfaces.rest;

import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import brandradar.crisisdetection.infrastructure.groq.GroqApiClient;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.sentimentintelligence.application.services.SentimentScoreCalculator;
import brandradar.shared.interfaces.rest.resources.DashboardResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/workspaces", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Dashboard", description = "Dashboard aggregated data")
public class DashboardController {

    private final BrandWorkspaceRepository workspaceRepository;
    private final BrandRepository brandRepository;
    private final MentionRepository mentionRepository;
    private final CrisisAlertRepository crisisAlertRepository;
    private final SentimentScoreCalculator sentimentScoreCalculator;
    private final GroqApiClient groqApiClient;

    public DashboardController(BrandWorkspaceRepository workspaceRepository,
                               BrandRepository brandRepository,
                               MentionRepository mentionRepository,
                               CrisisAlertRepository crisisAlertRepository,
                               SentimentScoreCalculator sentimentScoreCalculator,
                               GroqApiClient groqApiClient) {
        this.workspaceRepository = workspaceRepository;
        this.brandRepository = brandRepository;
        this.mentionRepository = mentionRepository;
        this.crisisAlertRepository = crisisAlertRepository;
        this.sentimentScoreCalculator = sentimentScoreCalculator;
        this.groqApiClient = groqApiClient;
    }

    @Operation(summary = "Get dashboard data for a workspace")
    @GetMapping("/{workspaceId}/dashboard")
    public ResponseEntity<DashboardResource> getDashboard(@PathVariable Long workspaceId) {
        var workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isEmpty()) return ResponseEntity.notFound().build();

        var brands = brandRepository.findByWorkspaceId(workspaceId);
        if (brands.isEmpty()) return ResponseEntity.notFound().build();

        var brand = brands.get(0);
        var mentions = mentionRepository.findByBrandId(brand.getId());

        // Calcular sentiment score
        var score = sentimentScoreCalculator.calculateForBrand(
                brand.getId(), brand.getName(), mentions);
        var label = sentimentScoreCalculator.getLabel(score);

        // Calcular distribución de sentimiento
        long total = mentions.size();
        long positive = mentions.stream()
                .filter(m -> m.getSentimentCompound().doubleValue() > 0.3)
                .count();
        long negative = mentions.stream()
                .filter(m -> m.getSentimentCompound().doubleValue() < -0.3)
                .count();
        long neutral = total - positive - negative;

        double positivePercent = total > 0 ? (double) positive / total * 100 : 0;
        double negativePercent = total > 0 ? (double) negative / total * 100 : 0;
        double neutralPercent = total > 0 ? (double) neutral / total * 100 : 0;

        // Fuente más activa
        String topSource = mentions.stream()
                .collect(Collectors.groupingBy(Mention::getSourcePlatform, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("YOUTUBE");

        // Incidentes activos
        long activeIncidents = crisisAlertRepository.findByBrandId(brand.getId())
                .stream()
                .filter(a -> "OPEN".equals(a.getStatus()))
                .count();

        // Menciones recientes
        List<DashboardResource.MentionSummary> recentMentions = mentions.stream()
                .sorted((a, b) -> b.getPublishedAt().compareTo(a.getPublishedAt()))
                .limit(5)
                .map(m -> new DashboardResource.MentionSummary(
                        m.getContent(),
                        m.getSourcePlatform(),
                        m.getAuthor(),
                        m.getPublishedAt().toString()
                ))
                .toList();

        // Crisis analysis con Groq
        String crisisAnalysis = getCrisisAnalysis(brand.getName(), mentions, negativePercent);

        return ResponseEntity.ok(new DashboardResource(
                brand.getId(),
                brand.getName(),
                score.doubleValue(),
                label,
                total,
                Math.round(positivePercent * 10.0) / 10.0,
                Math.round(neutralPercent * 10.0) / 10.0,
                Math.round(negativePercent * 10.0) / 10.0,
                activeIncidents,
                topSource,
                recentMentions,
                crisisAnalysis
        ));
    }

    private String getCrisisAnalysis(String brandName, List<Mention> mentions, double negativePercent) {
        try {
            if (negativePercent < 30) return null;

            String negativeContent = mentions.stream()
                    .filter(m -> m.getSentimentCompound().doubleValue() < -0.3)
                    .limit(5)
                    .map(Mention::getContent)
                    .collect(Collectors.joining("\n"));

            String prompt = String.format("""
                    Analiza estas menciones negativas sobre la marca "%s" y genera un diagnóstico breve (máximo 2 oraciones) 
                    sobre el patrón de insatisfacción detectado. Sé específico y directo.
                    
                    Menciones:
                    %s
                    
                    Responde solo con el diagnóstico, sin introducción ni formato.
                    """, brandName, negativeContent);

            return groqApiClient.chat(prompt);
        } catch (Exception e) {
            return null;
        }
    }
}