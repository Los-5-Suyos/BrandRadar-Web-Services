package brandradar.shared.infrastructure.scheduling;

import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.crisisdetection.application.services.IncidentDetectionService;
import brandradar.reputationmonitoring.application.services.MentionIngestionService;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.sentimentintelligence.application.services.SentimentScoreCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DashboardRefreshScheduler {

    private final BrandWorkspaceRepository brandWorkspaceRepository;
    private final BrandRepository brandRepository;
    private final MentionIngestionService mentionIngestionService;
    private final SentimentScoreCalculator sentimentScoreCalculator;
    private final IncidentDetectionService incidentDetectionService;

    public DashboardRefreshScheduler(BrandWorkspaceRepository brandWorkspaceRepository,
                                     BrandRepository brandRepository,
                                     MentionIngestionService mentionIngestionService,
                                     SentimentScoreCalculator sentimentScoreCalculator,
                                     IncidentDetectionService incidentDetectionService) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
        this.brandRepository = brandRepository;
        this.mentionIngestionService = mentionIngestionService;
        this.sentimentScoreCalculator = sentimentScoreCalculator;
        this.incidentDetectionService = incidentDetectionService;
    }

    @Scheduled(fixedRate = 300000) // cada 5 minutos
    public void refreshDashboard() {
        log.info("DashboardRefreshScheduler - Starting refresh...");
        try {
            var workspaces = brandWorkspaceRepository.findAll();
            log.info("DashboardRefreshScheduler - Found {} workspaces", workspaces.size());

            for (var workspace : workspaces) {
                List<Brand> brands = brandRepository.findByWorkspaceId(workspace.getId());

                for (Brand brand : brands) {
                    log.info("DashboardRefreshScheduler - Processing brand id={} name={}",
                            brand.getId(), brand.getName());

                    // 1. Ingestar menciones reales
                    List<Mention> mentions = mentionIngestionService.ingestForBrand(
                            brand.getId(), brand.getName());

                    // 2. Calcular sentiment score con Groq
                    var score = sentimentScoreCalculator.calculateForBrand(
                            brand.getId(), brand.getName(), mentions);
                    log.info("DashboardRefreshScheduler - Score={} for brand={}",
                            score, brand.getName());

                    // 3. Detectar incidentes
                    incidentDetectionService.detectForBrand(
                            brand.getId(), brand.getName(), mentions);
                }
            }
            log.info("DashboardRefreshScheduler - Refresh completed");
        } catch (Exception e) {
            log.error("DashboardRefreshScheduler - Error: {}", e.getMessage());
        }
    }
}