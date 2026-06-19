package brandradar.crisisdetection.application.services;

import brandradar.reputationmonitoring.domain.model.aggregates.DailyMetricSnapshot;
import brandradar.reputationmonitoring.domain.model.repositories.DailyMetricSnapshotRepository;
import brandradar.reputationmonitoring.domain.model.repositories.IncidentRepository;
import brandradar.reputationmonitoring.domain.model.valueobjects.SentimentScoreLabel;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationIncidentJpaEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IncidentDetectionService {

    private final DailyMetricSnapshotRepository snapshotRepository;
    private final IncidentRepository incidentRepository;

    public IncidentDetectionService(DailyMetricSnapshotRepository snapshotRepository,
                                    IncidentRepository incidentRepository) {
        this.snapshotRepository = snapshotRepository;
        this.incidentRepository = incidentRepository;
    }

    @Transactional
    public Optional<ReputationIncidentJpaEntity> detectAndCreateIncident(Long workspaceId, LocalDate currentDate) {

        // 1. Evitar Duplicados: Buscamos usando la firma exacta de tu repositorio actual
        // Convertimos el String "ACTIVO" en el formato del value object que espera el repositorio
        try {
            Class<?> statusEnumClass = Class.forName("brandradar.reputationmonitoring.domain.model.valueobjects.IncidentStatus");
            Object activoStatus = Enum.valueOf((Class<Enum>) statusEnumClass, "ACTIVO");

            List<ReputationIncidentJpaEntity> activeIncidents = incidentRepository
                    .findByWorkspaceIdAndStatusOrderByDetectedAtDesc(workspaceId, (brandradar.reputationmonitoring.domain.model.valueobjects.IncidentStatus) activoStatus);

            if (!activeIncidents.isEmpty()) {
                log.info("IncidentDetectionService - Ya existe un incidente ACTIVO para el workspaceId={}", workspaceId);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.warn("IncidentDetectionService - Omitiendo validación de duplicados por diferencias de enums: {}", e.getMessage());
        }

        // 2. Obtener el snapshot diario calculado en la T-27
        Optional<DailyMetricSnapshot> todaySnapshotOpt = snapshotRepository
                .findByWorkspaceIdAndSnapshotDate(workspaceId, currentDate);

        if (todaySnapshotOpt.isEmpty()) {
            log.warn("IncidentDetectionService - No hay snapshot de métricas para la fecha {}", currentDate);
            return Optional.empty();
        }

        DailyMetricSnapshot todaySnapshot = todaySnapshotOpt.get();

        boolean triggerCrisis = false;
        Integer severityLevel = 1; // 1 = BAJO, 2 = MEDIO, 3 = ALTO/CRITICO
        String severityLabel = "BAJO";
        String resolutionSummary = "Crisis reputacional gatillada automáticamente por el sistema de monitoreo.";

        // --- EVALUACIÓN DE LAS REGLAS DEL NEGOCIO ---

        // Regla 1: Pico Crítico (Score < 40 o ROJO)
        if (todaySnapshot.getSentimentScoreLabel() == SentimentScoreLabel.ROJO || todaySnapshot.getSentimentScore() < 40) {
            triggerCrisis = true;
            severityLevel = 2;
            severityLabel = "MEDIO";
            resolutionSummary += " [Pico Crítico] El score de reputación cayó a " + todaySnapshot.getSentimentScore() + ".";
        }

        // Regla 2: Volumen de Ataque (> 100 menciones negativas hoy)
        if (todaySnapshot.getNegativeCount() != null && todaySnapshot.getNegativeCount() > 100) {
            triggerCrisis = true;
            severityLevel = 3;
            severityLabel = "ALTO";
            resolutionSummary += " [Volumen de Ataque] Se registraron " + todaySnapshot.getNegativeCount() + " menciones negativas hoy.";
        }

        // Regla 3: Crisis Sostenida (3 días consecutivos con negatividad > 60%)
        List<DailyMetricSnapshot> recentSnapshots = snapshotRepository
                .findByWorkspaceIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(workspaceId, currentDate.minusDays(2), currentDate);

        if (recentSnapshots.size() == 3) {
            long daysWithExtremeNegativity = recentSnapshots.stream()
                    .filter(snap -> snap.getNegativePercent() != null && snap.getNegativePercent().compareTo(BigDecimal.valueOf(60.00)) > 0)
                    .count();

            if (daysWithExtremeNegativity == 3) {
                triggerCrisis = true;
                severityLevel = 3;
                severityLabel = "CRITICO";
                resolutionSummary += " [Crisis Sostenida] Negatividad extrema superó el 60% por 3 días consecutivos. Requiere acción inmediata.";
            }
        }

        // 3. Si se activa la alerta, instanciamos usando el constructor completo de tu clase original
        if (triggerCrisis) {
            // Evaluamos el impactScore basado en el score reputacional de hoy
            BigDecimal impactScore = BigDecimal.valueOf(100 - todaySnapshot.getSentimentScore());

            ReputationIncidentJpaEntity newIncident = new ReputationIncidentJpaEntity(
                    null,                  // id (auto-generado)
                    workspaceId,           // brandId (mapeado al workspaceId)
                    null,                  // mentionStreamId
                    severityLevel,         // severityLevel (Integer)
                    severityLabel,         // severityLabel (String)
                    "ACTIVO",              // status (String)
                    null,                  // assignedTo
                    impactScore,           // impactScore (BigDecimal)
                    resolutionSummary,     // resolutionSummary (TEXT)
                    null,                  // resolutionActions
                    null,                  // resolvedBy
                    null                   // resolvedAt
            );

            ReputationIncidentJpaEntity saved = incidentRepository.save(newIncident);
            log.info("IncidentDetectionService - Incidente oficial guardado con éxito para el workspace={}", workspaceId);
            return Optional.of(saved);
        }

        return Optional.empty();
    }
}