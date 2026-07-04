package brandradar.crisisdetection.application.services;

import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.KeywordRuleJpaEntity;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataKeywordRuleRepository;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import brandradar.iam.application.services.NotificationService;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationIncidentRepository;
import brandradar.sentimentintelligence.domain.model.repositories.DashboardSnapshotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class IncidentDetectionService {

    private static final int VOLUME_SPIKE_MULTIPLIER = 3;
    private static final int VOLUME_HISTORY_DAYS = 7;
    private static final int CRITICAL_KEYWORD_MIN_OCCURRENCES = 3;

    private final CrisisAlertRepository crisisAlertRepository;
    private final ReputationIncidentRepository reputationIncidentRepository;
    private final AlertPreferenceService alertPreferenceService;
    private final NotificationService notificationService;
    private final BrandRepository brandRepository;
    private final BrandWorkspaceRepository brandWorkspaceRepository;
    private final DashboardSnapshotRepository dashboardSnapshotRepository;
    private final SpringDataKeywordRuleRepository keywordRuleRepository;

    public IncidentDetectionService(CrisisAlertRepository crisisAlertRepository,
                                    ReputationIncidentRepository reputationIncidentRepository,
                                    AlertPreferenceService alertPreferenceService,
                                    NotificationService notificationService,
                                    BrandRepository brandRepository,
                                    BrandWorkspaceRepository brandWorkspaceRepository,
                                    DashboardSnapshotRepository dashboardSnapshotRepository,
                                    SpringDataKeywordRuleRepository keywordRuleRepository) {
        this.crisisAlertRepository = crisisAlertRepository;
        this.reputationIncidentRepository = reputationIncidentRepository;
        this.alertPreferenceService = alertPreferenceService;
        this.notificationService = notificationService;
        this.brandRepository = brandRepository;
        this.brandWorkspaceRepository = brandWorkspaceRepository;
        this.dashboardSnapshotRepository = dashboardSnapshotRepository;
        this.keywordRuleRepository = keywordRuleRepository;
    }

    @Transactional
    public void detectForBrand(Long brandId, String brandName, List<Mention> mentions) {
        if (mentions == null || mentions.isEmpty()) return;

        long negativeCount = mentions.stream()
                .filter(m -> m.getSentimentCompound().doubleValue() < -0.3)
                .count();
        long totalCount = mentions.size();
        double negativeRatio = (double) negativeCount / totalCount;

        log.info("IncidentDetectionService - brand={} negatives={}/{} ratio={}",
                brandName, negativeCount, totalCount, negativeRatio);

        checkVolumeSpike(brandId, brandName, mentions);
        checkCriticalKeywords(brandId, brandName, mentions);

        if (negativeRatio >= 0.50 && negativeCount >= 5
                && alertPreferenceService.isEnabled(brandId, AlertPreferenceService.NEGATIVE_SPIKE)) {
            createAlertAndIncident(brandId, brandName, null,
                    "Pico de menciones negativas",
                    String.format("El %.0f%% de las menciones recientes sobre %s son negativas.",
                            negativeRatio * 100, brandName),
                    "SENTIMENT", "MEDIO");
        }

        if (negativeRatio >= 0.70 && negativeCount >= 3
                && alertPreferenceService.isEnabled(brandId, AlertPreferenceService.NEGATIVE_SPIKE)) {
            createAlertAndIncident(brandId, brandName, null,
                    "Crisis de reputación crítica",
                    String.format("El %.0f%% de las menciones sobre %s son negativas. Requiere acción inmediata.",
                            negativeRatio * 100, brandName),
                    "COMBINED", "CRITICO");
        }
    }

    private void checkVolumeSpike(Long brandId, String brandName, List<Mention> allMentions) {
        if (!alertPreferenceService.isEnabled(brandId, AlertPreferenceService.HIGH_VOLUME)) return;

        var history = dashboardSnapshotRepository.findLastNDaysByBrandId(brandId, VOLUME_HISTORY_DAYS);
        if (history.size() < 3) return;

        double averageDaily = history.stream()
                .mapToLong(s -> s.getMentionsCount() != null ? s.getMentionsCount() : 0)
                .average()
                .orElse(0);
        if (averageDaily <= 0) return;

        var today = LocalDate.now(ZoneId.of("America/Lima"));
        long todayCount = allMentions.stream()
                .filter(m -> m.getPublishedAt() != null
                        && m.getPublishedAt().atZone(ZoneId.of("America/Lima")).toLocalDate().equals(today))
                .count();

        if (todayCount >= averageDaily * VOLUME_SPIKE_MULTIPLIER) {
            createAlertAndIncident(brandId, brandName, null,
                    "Volumen inusual de menciones",
                    String.format("Hoy se registraron %d menciones, %.1fx tu promedio diario (%.0f).",
                            todayCount, todayCount / averageDaily, averageDaily),
                    "VOLUME", "ALTO");
        }
    }

    private void checkCriticalKeywords(Long brandId, String brandName, List<Mention> allMentions) {
        if (!alertPreferenceService.isEnabled(brandId, AlertPreferenceService.CRITICAL_KEYWORD)) return;

        var negativeMentions = allMentions.stream()
                .filter(m -> m.getSentimentCompound().doubleValue() < -0.3)
                .toList();
        if (negativeMentions.isEmpty()) return;

        var keywords = keywordRuleRepository.findByBrandIdAndIsActiveTrue(brandId);

        for (KeywordRuleJpaEntity keyword : keywords) {
            var kw = keyword.getKeyword().toLowerCase(Locale.ROOT);
            long occurrences = negativeMentions.stream()
                    .filter(m -> m.getContent() != null && m.getContent().toLowerCase(Locale.ROOT).contains(kw))
                    .count();

            if (occurrences >= CRITICAL_KEYWORD_MIN_OCCURRENCES) {
                createAlertAndIncident(brandId, brandName, null,
                        "Keyword crítica detectada: \"" + keyword.getKeyword() + "\"",
                        String.format("La palabra \"%s\" apareció en %d menciones negativas sobre %s.",
                                keyword.getKeyword(), occurrences, brandName),
                        "KEYWORD", "MEDIO");
            }
        }
    }

    /**
     * Crea la CrisisAlert (el aviso) Y su ReputationIncident vinculado (el caso con
     * seguimiento) — son 2 registros distintos: la alerta es la detección/notificación,
     * el incidente es el expediente que se investiga y resuelve.
     */
    private void createAlertAndIncident(Long brandId, String brandName, Long mentionStreamId,
                                        String title, String description, String triggerType, String severity) {
        try {
            Integer priorityLevel = switch (severity) {
                case "CRITICO" -> 3;
                case "ALTO" -> 2;
                default -> 1;
            };

            var alert = CrisisAlert.create(
                    brandId, mentionStreamId, null, priorityLevel, severity, title, description,
                    triggerType, BigDecimal.ZERO, BigDecimal.valueOf(0.85)
            );
            var savedAlert = crisisAlertRepository.save(alert);
            log.info("IncidentDetectionService - Alert created: {} for brand={}", title, brandName);

            var incident = ReputationIncident.create(
                    brandId, mentionStreamId, savedAlert.getId(), priorityLevel, severity, title, description);
            reputationIncidentRepository.save(incident);
            log.info("IncidentDetectionService - Incident linked to alert id={}", savedAlert.getId());

            if (alertPreferenceService.isEnabled(brandId, AlertPreferenceService.NEW_INCIDENT)) {
                notifyOwner(brandId, title, description);
            }
        } catch (Exception e) {
            log.error("IncidentDetectionService - Error creating alert/incident: {}", e.getMessage());
        }
    }

    private void notifyOwner(Long brandId, String title, String message) {
        try {
            var brand = brandRepository.findById(brandId).orElse(null);
            if (brand == null) return;
            var workspace = brandWorkspaceRepository.findById(brand.getWorkspaceId()).orElse(null);
            if (workspace == null) return;

            notificationService.notify(workspace.getUserId(), brandId,
                    NotificationService.TYPE_CRISIS_ALERT, title, message);
        } catch (Exception e) {
            log.error("IncidentDetectionService - Error sending notification: {}", e.getMessage());
        }
    }
}