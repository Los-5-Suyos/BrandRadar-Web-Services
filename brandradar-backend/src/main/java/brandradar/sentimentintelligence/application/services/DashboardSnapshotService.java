package brandradar.sentimentintelligence.application.services;

import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.crisisdetection.application.services.AlertPreferenceService;
import brandradar.iam.application.services.NotificationService;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.sentimentintelligence.domain.model.aggregates.DashboardSnapshot;
import brandradar.sentimentintelligence.domain.model.repositories.DashboardSnapshotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
public class DashboardSnapshotService {

    private static final double SCORE_DROP_THRESHOLD = 10.0; // puntos, coincide con "más de 10 puntos en 24h" del frontend

    private final DashboardSnapshotRepository snapshotRepository;
    private final InsightGenerationService insightGenerationService;
    private final AlertPreferenceService alertPreferenceService;
    private final NotificationService notificationService;
    private final BrandRepository brandRepository;
    private final BrandWorkspaceRepository brandWorkspaceRepository;

    public DashboardSnapshotService(DashboardSnapshotRepository snapshotRepository,
                                    InsightGenerationService insightGenerationService,
                                    AlertPreferenceService alertPreferenceService,
                                    NotificationService notificationService,
                                    BrandRepository brandRepository,
                                    BrandWorkspaceRepository brandWorkspaceRepository) {
        this.snapshotRepository = snapshotRepository;
        this.insightGenerationService = insightGenerationService;
        this.alertPreferenceService = alertPreferenceService;
        this.notificationService = notificationService;
        this.brandRepository = brandRepository;
        this.brandWorkspaceRepository = brandWorkspaceRepository;
    }

    @Transactional
    public void recordSnapshot(Long brandId, String brandName, BigDecimal sentimentScore, List<Mention> allMentions) {
        var today = LocalDate.now(ZoneId.of("America/Lima"));

        long total = allMentions.size();
        long positive = allMentions.stream()
                .filter(m -> m.getSentimentCompound().doubleValue() > 0.3)
                .count();
        long negative = allMentions.stream()
                .filter(m -> m.getSentimentCompound().doubleValue() < -0.3)
                .count();
        long neutral = total - positive - negative;

        BigDecimal positivePct = percentage(positive, total);
        BigDecimal neutralPct = percentage(neutral, total);
        BigDecimal negativePct = percentage(negative, total);

        // Genera (o re-genera) los insights por canal — con fallback automático si Groq falla.
        insightGenerationService.generateChannelInsights(brandId, brandName, allMentions);

        // Genera el diagnóstico de crisis (solo si negatividad > 30%, mismo umbral de siempre).
        String crisisAnalysisText = insightGenerationService.generateCrisisAnalysis(
                brandName, allMentions, negativePct.doubleValue());

        // Notifica si el score cayó más de 10 puntos vs. ayer (respeta la preferencia SCORE_DROP).
        checkScoreDrop(brandId, brandName, today, sentimentScore);

        var existing = snapshotRepository.findByBrandIdAndDate(brandId, today);

        DashboardSnapshot toSave;
        if (existing.isPresent()) {
            toSave = existing.get().withValues(sentimentScore, total, positivePct, neutralPct,
                    negativePct, crisisAnalysisText);
        } else {
            toSave = DashboardSnapshot.create(brandId, today, sentimentScore, total,
                    positivePct, neutralPct, negativePct, crisisAnalysisText);
        }

        snapshotRepository.save(toSave);
        log.info("DashboardSnapshotService - Snapshot saved for brandId={} date={} score={}",
                brandId, today, sentimentScore);
    }

    private void checkScoreDrop(Long brandId, String brandName, LocalDate today, BigDecimal todayScore) {
        if (!alertPreferenceService.isEnabled(brandId, AlertPreferenceService.SCORE_DROP)) return;

        var yesterday = today.minusDays(1);
        var yesterdaySnapshot = snapshotRepository.findByBrandIdAndDate(brandId, yesterday);
        if (yesterdaySnapshot.isEmpty()) return;

        double drop = yesterdaySnapshot.get().getSentimentScore().doubleValue() - todayScore.doubleValue();
        if (drop < SCORE_DROP_THRESHOLD) return;

        try {
            var brand = brandRepository.findById(brandId).orElse(null);
            if (brand == null) return;
            var workspace = brandWorkspaceRepository.findById(brand.getWorkspaceId()).orElse(null);
            if (workspace == null) return;

            notificationService.notify(workspace.getUserId(), brandId,
                    NotificationService.TYPE_SCORE_DROP,
                    "La reputación de " + brandName + " bajó",
                    String.format("El sentiment score cayó %.1f puntos respecto a ayer (%.1f → %.1f).",
                            drop, yesterdaySnapshot.get().getSentimentScore().doubleValue(), todayScore.doubleValue()));
        } catch (Exception e) {
            log.error("DashboardSnapshotService - Error sending score-drop notification: {}", e.getMessage());
        }
    }

    private BigDecimal percentage(long count, long total) {
        if (total == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(count)
                .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(1, RoundingMode.HALF_UP);
    }
}