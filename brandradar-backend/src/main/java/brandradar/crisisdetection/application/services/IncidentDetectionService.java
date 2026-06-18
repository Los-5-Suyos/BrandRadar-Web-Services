package brandradar.crisisdetection.application.services;

import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class IncidentDetectionService {

    private final CrisisAlertRepository crisisAlertRepository;

    public IncidentDetectionService(CrisisAlertRepository crisisAlertRepository) {
        this.crisisAlertRepository = crisisAlertRepository;
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

        // Regla 1: Volume surge — más de 100 menciones negativas → ALTO
        if (negativeCount >= 100) {
            createAlertIfNotExists(brandId, brandName,
                    "Volume surge detectado",
                    "Se detectaron " + negativeCount + " menciones negativas en las últimas horas.",
                    "ALTO");
        }

        // Regla 2: Ratio negativo >= 50% → MEDIO
        if (negativeRatio >= 0.50 && negativeCount >= 5) {
            createAlertIfNotExists(brandId, brandName,
                    "Pico de menciones negativas",
                    String.format("El %.0f%% de las menciones recientes sobre %s son negativas.",
                            negativeRatio * 100, brandName),
                    "MEDIO");
        }

        // Regla 3: Ratio negativo >= 70% → ALTO + acción inmediata
        if (negativeRatio >= 0.70 && negativeCount >= 3) {
            createAlertIfNotExists(brandId, brandName,
                    "Crisis de reputación crítica",
                    String.format("El %.0f%% de las menciones sobre %s son negativas. Requiere acción inmediata.",
                            negativeRatio * 100, brandName),
                    "CRITICO");
        }
    }

    private void createAlertIfNotExists(Long brandId, String brandName,
                                        String title, String description, String severity) {
        try {
            Integer priorityLevel = switch (severity) {
                case "CRITICO" -> 3;
                case "ALTO" -> 2;
                default -> 1;
            };

            var alert = CrisisAlert.create(
                    brandId,
                    null,           // mentionStreamId
                    null,           // monitoringRuleId
                    priorityLevel,  // priorityLevel
                    severity,       // priorityLabel
                    title,          // triggerType
                    BigDecimal.ZERO, // triggerDeviationPct
                    BigDecimal.valueOf(0.85) // triggerConfidence
            );
            crisisAlertRepository.save(alert);
            log.info("IncidentDetectionService - Alert created: {} for brand={}", title, brandName);
        } catch (Exception e) {
            log.error("IncidentDetectionService - Error creating alert: {}", e.getMessage());
        }
    }
}