package brandradar.shared.infrastructure.scheduling;

import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.crisisdetection.application.services.IncidentDetectionService;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
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
    private final MentionRepository mentionRepository;
    private final SentimentScoreCalculator sentimentScoreCalculator;
    private final IncidentDetectionService incidentDetectionService;

    public DashboardRefreshScheduler(BrandWorkspaceRepository brandWorkspaceRepository, BrandRepository brandRepository, MentionRepository mentionRepository, SentimentScoreCalculator sentimentScoreCalculator, IncidentDetectionService incidentDetectionService) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
        this.brandRepository = brandRepository;
        this.mentionRepository = mentionRepository;
        this.sentimentScoreCalculator = sentimentScoreCalculator;
        this.incidentDetectionService = incidentDetectionService;
    }

    @Scheduled(fixedRate = 300000) // cada 5 minutos
    public void refreshDashboard() {
        log.info("DashboardRefreshScheduler - Starting recalculation (no external ingestion)...");
        try {
            var workspaces = brandWorkspaceRepository.findAll();
            log.info("DashboardRefreshScheduler - Found {} workspaces", workspaces.size());

            for (var workspace : workspaces) {
                List<Brand> brands = brandRepository.findByWorkspaceId(workspace.getId());

                for (Brand brand : brands) {
                    List<Mention> mentions = mentionRepository.findByBrandId(brand.getId());
                    if (mentions.isEmpty()) {
                        continue;
                    }

                    log.info("DashboardRefreshScheduler - Recalculating brand id={} name={} with {} stored mentions",
                            brand.getId(), brand.getName(), mentions.size());

                    var score = sentimentScoreCalculator.calculateForBrand(
                            brand.getId(), brand.getName(), mentions);
                    log.info("DashboardRefreshScheduler - Score={} for brand={}", score, brand.getName());

                    incidentDetectionService.detectForBrand(
                            brand.getId(), brand.getName(), mentions);
                }
            }
            log.info("DashboardRefreshScheduler - Recalculation completed");
        } catch (Exception e) {
            log.error("DashboardRefreshScheduler - Error: {}", e.getMessage());
        }
    }
}