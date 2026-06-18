package brandradar.reputationmonitoring.application.services;

import brandradar.reputationmonitoring.domain.model.aggregates.DailyMetricSnapshot;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.DailyMetricSnapshotRepository;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentIndex;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentScoreLabel;
import brandradar.reputationmonitoring.domain.model.valueobjects.SourceType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DailyMetricsService {

    private final MentionRepository mentionRepository;
    private final DailyMetricSnapshotRepository snapshotRepository;
    private final SentimentScoreCalculator sentimentScoreCalculator;

    public DailyMetricsService(MentionRepository mentionRepository,
                               DailyMetricSnapshotRepository snapshotRepository,
                               SentimentScoreCalculator sentimentScoreCalculator) {
        this.mentionRepository = mentionRepository;
        this.snapshotRepository = snapshotRepository;
        this.sentimentScoreCalculator = sentimentScoreCalculator;
    }

    @Transactional
    public DailyMetricSnapshot calculateAndPersist(Long workspaceId, LocalDate date) {
        // [PASO 2] Recuperar menciones del día: publishedAt entre [date 00:00, date 23:59]
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);

        List<Mention> mentions = mentionRepository
                .findByWorkspaceIdAndPublishedAtBetweenAndIsActiveTrue(
                        workspaceId,
                        startOfDay.toInstant(ZoneOffset.UTC),
                        endOfDay.toInstant(ZoneOffset.UTC)
                );

        int totalMentions = mentions.size();

        // [PASO 3] Calcular counters de sentimientos
        int negativeCount = 0;
        int neutralCount = 0;
        int positiveCount = 0;

        for (Mention mention : mentions) {
            if (mention.getSentimentLabel() != null) {
                switch (mention.getSentimentLabel()) {
                    case NEG -> negativeCount++;
                    case NEU -> neutralCount++;
                    case POS -> positiveCount++;
                }
            }
        }

        // Calcular porcentajes usando BigDecimals (evitando división por cero si total es 0)
        BigDecimal negativePercent = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal neutralPercent = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal positivePercent = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        if (totalMentions > 0) {
            BigDecimal totalBd = BigDecimal.valueOf(totalMentions);
            negativePercent = BigDecimal.valueOf(negativeCount).multiply(BigDecimal.valueOf(100)).divide(totalBd, 2, RoundingMode.HALF_UP);
            neutralPercent = BigDecimal.valueOf(neutralCount).multiply(BigDecimal.valueOf(100)).divide(totalBd, 2, RoundingMode.HALF_UP);
            positivePercent = BigDecimal.valueOf(positiveCount).multiply(BigDecimal.valueOf(100)).divide(totalBd, 2, RoundingMode.HALF_UP);
        }

        // [PASO 4] Invocar SentimentScoreCalculator.calculate(mentions) para obtener score y label
        // Se invoca SIEMPRE, incluso si la lista viene vacía (totalMentions == 0) tal como pide la especificación.
        SentimentScoreResult scoreResult = sentimentScoreCalculator.calculate(mentions);
        int sentimentScore = scoreResult.score();
        SentimentScoreLabel sentimentScoreLabel = scoreResult.label();

        // [PASO 5] Calcular variationVsYesterday: comparar totalMentions con snapshot de D-1. Si no existe -> null
        BigDecimal variationVsYesterday = null;
        Optional<DailyMetricSnapshot> yesterdaySnapshotOpt = snapshotRepository
                .findByWorkspaceIdAndSnapshotDate(workspaceId, date.minusDays(1));

        if (yesterdaySnapshotOpt.isPresent()) {
            int yesterdayTotal = yesterdaySnapshotOpt.get().getTotalMentions();
            if (yesterdayTotal > 0) {
                double variation = ((double) (totalMentions - yesterdayTotal) / yesterdayTotal) * 100.0;
                variationVsYesterday = BigDecimal.valueOf(variation).setScale(2, RoundingMode.HALF_UP);
            }
            // Si existía pero el total de ayer era 0, se queda en null de forma segura.
        }

        // [PASO 6] Detectar topSource y calcular su SentimentIndex
        SourceType topSource = SourceType.TWITTER; // Default requerido por las validaciones de inicialización de la entidad
        SentimentIndex topSourceSentimentIndex = SentimentIndex.BAJO;

        if (totalMentions > 0) {
            Map<SourceType, List<Mention>> mentionsBySource = mentions.stream()
                    .collect(Collectors.groupingBy(Mention::getSource));

            Map.Entry<SourceType, List<Mention>> maxSourceEntry = null;
            for (Map.Entry<SourceType, List<Mention>> entry : mentionsBySource.entrySet()) {
                if (maxSourceEntry == null || entry.getValue().size() > maxSourceEntry.getValue().size()) {
                    maxSourceEntry = entry;
                }
            }

            if (maxSourceEntry != null) {
                topSource = maxSourceEntry.getKey();
                List<Mention> topSourceMentions = maxSourceEntry.getValue();

                // Promedio de scores de esa fuente específica
                double averageScore = topSourceMentions.stream()
                        .mapToDouble(m -> m.getSentimentScore().doubleValue())
                        .average()
                        .orElse(0.0);

                // Asignación estricta de SentimentIndex: ALTO (≥ 0.65), MEDIO (≥ 0.44), BAJO (< 0.40)
                if (averageScore >= 0.65) {
                    topSourceSentimentIndex = SentimentIndex.ALTO;
                } else if (averageScore >= 0.40) {
                    topSourceSentimentIndex = SentimentIndex.MEDIO;
                } else {
                    topSourceSentimentIndex = SentimentIndex.BAJO;
                }
            }
        }

        // [PASO 7] Upsert en DailyMetricSnapshotRepository: si ya existe, actualizar en lugar de crear
        Optional<DailyMetricSnapshot> existingSnapshotOpt = snapshotRepository
                .findByWorkspaceIdAndSnapshotDate(workspaceId, date);

        DailyMetricSnapshot snapshotToSave;

        if (existingSnapshotOpt.isPresent()) {
            // Rehydrate nos permite mapear las propiedades conservando el ID existente para que JPA haga un UPDATE
            snapshotToSave = DailyMetricSnapshot.rehydrate(
                    existingSnapshotOpt.get().getId(),
                    workspaceId,
                    date,
                    sentimentScore,
                    sentimentScoreLabel,
                    totalMentions,
                    negativeCount,
                    neutralCount,
                    positiveCount,
                    negativePercent,
                    neutralPercent,
                    positivePercent,
                    variationVsYesterday,
                    topSource,
                    topSourceSentimentIndex,
                    Instant.now()
            );
        } else {
            // Fábrica estática para registros nuevos
            snapshotToSave = DailyMetricSnapshot.create(
                    workspaceId,
                    date,
                    sentimentScore,
                    sentimentScoreLabel,
                    totalMentions,
                    negativeCount,
                    neutralCount,
                    positiveCount,
                    negativePercent,
                    neutralPercent,
                    positivePercent,
                    variationVsYesterday,
                    topSource,
                    topSourceSentimentIndex,
                    Instant.now()
            );
        }

        return snapshotRepository.save(snapshotToSave);
    }
}