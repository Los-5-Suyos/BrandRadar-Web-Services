package brandradar.shared.interfaces.rest;

import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.domain.model.repositories.KeywordRuleRepository;
import brandradar.crisisdetection.application.services.IncidentDetectionService;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import brandradar.reputationmonitoring.application.services.MentionIngestionService;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.sentimentintelligence.application.services.DashboardSnapshotService;
import brandradar.sentimentintelligence.application.services.SentimentScoreCalculator;
import brandradar.sentimentintelligence.domain.model.repositories.ChannelInsightRepository;
import brandradar.sentimentintelligence.domain.model.repositories.DashboardSnapshotRepository;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import brandradar.shared.interfaces.rest.resources.CriticalKeywordsResource;
import brandradar.shared.interfaces.rest.resources.DashboardChannelsResource;
import brandradar.shared.interfaces.rest.resources.DashboardResource;
import brandradar.shared.interfaces.rest.resources.DashboardTrendResource;
import brandradar.shared.interfaces.rest.resources.RefreshResultResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/workspaces", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Dashboard", description = "Dashboard aggregated data")
public class DashboardController {

    private final BrandWorkspaceRepository workspaceRepository;
    private final BrandRepository brandRepository;
    private final MentionRepository mentionRepository;
    private final CrisisAlertRepository crisisAlertRepository;
    private final SentimentScoreCalculator sentimentScoreCalculator;
    private final OwnershipGuard ownershipGuard;
    private final MentionIngestionService mentionIngestionService;
    private final IncidentDetectionService incidentDetectionService;
    private final DashboardSnapshotService dashboardSnapshotService;
    private final DashboardSnapshotRepository dashboardSnapshotRepository;
    private final KeywordRuleRepository keywordRuleRepository;
    private final ChannelInsightRepository channelInsightRepository;

    public DashboardController(BrandWorkspaceRepository workspaceRepository,
                               BrandRepository brandRepository,
                               MentionRepository mentionRepository,
                               CrisisAlertRepository crisisAlertRepository,
                               SentimentScoreCalculator sentimentScoreCalculator,
                               OwnershipGuard ownershipGuard,
                               MentionIngestionService mentionIngestionService,
                               IncidentDetectionService incidentDetectionService,
                               DashboardSnapshotService dashboardSnapshotService,
                               DashboardSnapshotRepository dashboardSnapshotRepository,
                               KeywordRuleRepository keywordRuleRepository,
                               ChannelInsightRepository channelInsightRepository) {
        this.workspaceRepository = workspaceRepository;
        this.brandRepository = brandRepository;
        this.mentionRepository = mentionRepository;
        this.crisisAlertRepository = crisisAlertRepository;
        this.sentimentScoreCalculator = sentimentScoreCalculator;
        this.ownershipGuard = ownershipGuard;
        this.mentionIngestionService = mentionIngestionService;
        this.incidentDetectionService = incidentDetectionService;
        this.dashboardSnapshotService = dashboardSnapshotService;
        this.dashboardSnapshotRepository = dashboardSnapshotRepository;
        this.keywordRuleRepository = keywordRuleRepository;
        this.channelInsightRepository = channelInsightRepository;
    }

    @Operation(summary = "Get dashboard data for a workspace")
    @GetMapping("/{workspaceId}/dashboard")
    public ResponseEntity<DashboardResource> getDashboard(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        var brands = brandRepository.findByWorkspaceId(workspaceId);
        if (brands.isEmpty()) return ResponseEntity.notFound().build();

        var brand = brands.get(0);
        var mentions = mentionRepository.findByBrandId(brand.getId());

        var score = sentimentScoreCalculator.calculateForBrand(
                brand.getId(), brand.getName(), mentions);
        var label = sentimentScoreCalculator.getLabel(score);

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

        String topSource = mentions.stream()
                .collect(Collectors.groupingBy(Mention::getSourcePlatform, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("YOUTUBE");

        var openAlerts = crisisAlertRepository.findByBrandId(brand.getId())
                .stream()
                .filter(a -> "OPEN".equals(a.getStatus()))
                .sorted(Comparator.comparing(CrisisAlert::getPriorityLevel).reversed())
                .toList();
        var incidentItems = openAlerts.stream()
                .limit(3)
                .map(a -> new DashboardResource.IncidentItem(a.getId(), incidentTitle(a)))
                .toList();
        var incidentsSummary = new DashboardResource.IncidentsSummary((long) openAlerts.size(), incidentItems);

        var today = LocalDate.now(ZoneId.of("America/Lima"));
        long mentionsToday = mentions.stream()
                .filter(m -> m.getPublishedAt() != null
                        && m.getPublishedAt().atZone(ZoneId.of("America/Lima")).toLocalDate().equals(today))
                .count();

        var todaySnapshot = dashboardSnapshotRepository.findByBrandIdAndDate(brand.getId(), today);
        var yesterday = today.minusDays(1);
        var yesterdaySnapshot = dashboardSnapshotRepository.findByBrandIdAndDate(brand.getId(), yesterday);

        Double scoreDelta = null;
        Double mentionsDeltaPercent = null;
        if (yesterdaySnapshot.isPresent()) {
            var y = yesterdaySnapshot.get();
            scoreDelta = score.doubleValue() - y.getSentimentScore().doubleValue();
            if (y.getMentionsCount() != null && y.getMentionsCount() > 0) {
                mentionsDeltaPercent = ((double) (total - y.getMentionsCount()) / y.getMentionsCount()) * 100;
            }
        }

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

        // Ya no se llama a Groq aquí — se lee el texto que quedó guardado en el snapshot
        // de hoy (generado durante el último refresh).
        String crisisAnalysis = todaySnapshot.map(s -> s.getCrisisAnalysisText()).orElse(null);

        return ResponseEntity.ok(new DashboardResource(
                brand.getId(),
                brand.getName(),
                score.doubleValue(),
                label,
                scoreDelta != null ? Math.round(scoreDelta * 10.0) / 10.0 : null,
                total,
                mentionsToday,
                mentionsDeltaPercent != null ? Math.round(mentionsDeltaPercent * 10.0) / 10.0 : null,
                Math.round(positivePercent * 10.0) / 10.0,
                Math.round(neutralPercent * 10.0) / 10.0,
                Math.round(negativePercent * 10.0) / 10.0,
                incidentsSummary,
                topSource,
                recentMentions,
                crisisAnalysis,
                Instant.now()
        ));
    }

    @Operation(summary = "Get the sentiment score / mentions trend for the last N days (default 14)")
    @GetMapping("/{workspaceId}/dashboard/trend")
    public ResponseEntity<DashboardTrendResource> getTrend(
            @PathVariable Long workspaceId,
            @RequestParam(defaultValue = "14") int days) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        var brands = brandRepository.findByWorkspaceId(workspaceId);
        if (brands.isEmpty()) return ResponseEntity.notFound().build();
        var brand = brands.get(0);

        var snapshots = dashboardSnapshotRepository.findLastNDaysByBrandId(brand.getId(), days);
        var points = snapshots.stream()
                .sorted(Comparator.comparing(s -> s.getDate()))
                .map(s -> new DashboardTrendResource.TrendPoint(
                        s.getDate(), s.getSentimentScore().doubleValue(), s.getMentionsCount()))
                .toList();

        return ResponseEntity.ok(new DashboardTrendResource(brand.getId(), points));
    }

    @Operation(summary = "Get critical keywords ranking: how often each of the brand's " +
            "inclusion keywords appears in negative mentions, normalized against the most " +
            "frequent one (percentOfMax)")
    @GetMapping("/{workspaceId}/dashboard/critical-keywords")
    public ResponseEntity<CriticalKeywordsResource> getCriticalKeywords(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        var brands = brandRepository.findByWorkspaceId(workspaceId);
        if (brands.isEmpty()) return ResponseEntity.notFound().build();
        var brand = brands.get(0);

        var negativeMentions = mentionRepository.findByBrandId(brand.getId()).stream()
                .filter(m -> m.getSentimentCompound().doubleValue() < -0.3)
                .toList();

        var inclusionKeywords = keywordRuleRepository.findByBrandId(brand.getId());

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
                .map(e -> new CriticalKeywordsResource.KeywordCount(
                        e.getKey(), e.getValue(),
                        Math.round((double) e.getValue() / max * 1000.0) / 10.0))
                .toList();

        return ResponseEntity.ok(new CriticalKeywordsResource(brand.getId(), keywordCounts));
    }

    @Operation(summary = "Get sentiment index + insight per channel — el insight viene de Groq " +
            "generado durante el último refresh (con fallback a texto por reglas si Groq falla), " +
            "nunca se llama a Groq en esta petición.")
    @GetMapping("/{workspaceId}/dashboard/channels")
    public ResponseEntity<DashboardChannelsResource> getChannelScores(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        var brands = brandRepository.findByWorkspaceId(workspaceId);
        if (brands.isEmpty()) return ResponseEntity.notFound().build();
        var brand = brands.get(0);

        var mentions = mentionRepository.findByBrandId(brand.getId());
        var insightsByChannel = channelInsightRepository.findByBrandId(brand.getId()).stream()
                .collect(Collectors.toMap(i -> i.getChannelType(), i -> i.getInsightText()));

        var byChannel = mentions.stream()
                .filter(m -> m.getSourcePlatform() != null)
                .collect(Collectors.groupingBy(Mention::getSourcePlatform));

        var channels = byChannel.entrySet().stream()
                .map(entry -> {
                    var channelType = entry.getKey();
                    var channelMentions = entry.getValue();

                    double avgCompound = channelMentions.stream()
                            .mapToDouble(m -> m.getSentimentCompound() != null
                                    ? m.getSentimentCompound().doubleValue() : 0.0)
                            .average()
                            .orElse(0.0);
                    double sentimentIndex = (avgCompound + 1.0) / 2.0 * 100.0;

                    String insight = insightsByChannel.getOrDefault(channelType, "Sin datos suficientes");

                    return new DashboardChannelsResource.ChannelScore(
                            channelType,
                            Math.round(sentimentIndex * 10.0) / 10.0,
                            (long) channelMentions.size(),
                            insight
                    );
                })
                .sorted(Comparator.comparing(DashboardChannelsResource.ChannelScore::sentimentIndex))
                .toList();

        return ResponseEntity.ok(new DashboardChannelsResource(brand.getId(), channels));
    }

    @Operation(summary = "Trigger a manual refresh: ingest real mentions (YouTube + SociaVault) " +
            "for every brand in this workspace, recalculate sentiment, detect incidents, and " +
            "regenerate the dashboard insights. Gasta créditos de SociaVault y llama a Groq — " +
            "solo se dispara cuando el usuario aprieta 'Actualizar'.")
    @PostMapping("/{workspaceId}/refresh")
    public ResponseEntity<RefreshResultResource> refreshWorkspace(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        var brands = brandRepository.findByWorkspaceId(workspaceId);
        List<RefreshResultResource.BrandRefreshResult> results = brands.stream()
                .map(this::refreshBrand)
                .toList();

        return ResponseEntity.ok(new RefreshResultResource(workspaceId, results));
    }

    private RefreshResultResource.BrandRefreshResult refreshBrand(Brand brand) {
        try {
            var newMentions = mentionIngestionService.ingestForBrand(brand.getId(), brand.getName());

            var allMentions = mentionRepository.findByBrandId(brand.getId());
            var score = sentimentScoreCalculator.calculateForBrand(brand.getId(), brand.getName(), allMentions);
            incidentDetectionService.detectForBrand(brand.getId(), brand.getName(), allMentions);
            dashboardSnapshotService.recordSnapshot(brand.getId(), brand.getName(), score, allMentions);

            return new RefreshResultResource.BrandRefreshResult(
                    brand.getId(), brand.getName(), newMentions.size());
        } catch (Exception e) {
            log.error("Error refreshing brand {}: {}", brand.getId(), e.getMessage());
            return new RefreshResultResource.BrandRefreshResult(brand.getId(), brand.getName(), 0);
        }
    }

    private String incidentTitle(CrisisAlert alert) {
        return alert.getTitle() != null && !alert.getTitle().isBlank()
                ? alert.getTitle()
                : "Alerta: " + alert.getTriggerType();
    }
}