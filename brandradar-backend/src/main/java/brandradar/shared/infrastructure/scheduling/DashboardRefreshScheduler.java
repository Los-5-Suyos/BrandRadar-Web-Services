package brandradar.shared.infrastructure.scheduling;

import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.reputationmonitoring.application.services.MentionIngestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DashboardRefreshScheduler {

    private final BrandWorkspaceRepository brandWorkspaceRepository;
    private final BrandRepository brandRepository;
    private final MentionIngestionService mentionIngestionService;

    public DashboardRefreshScheduler(BrandWorkspaceRepository brandWorkspaceRepository,
                                     BrandRepository brandRepository,
                                     MentionIngestionService mentionIngestionService) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
        this.brandRepository = brandRepository;
        this.mentionIngestionService = mentionIngestionService;
    }

    @Scheduled(fixedRate = 300000) // cada 5 minutos
    public void refreshDashboard() {
        log.info("DashboardRefreshScheduler - Starting dashboard refresh...");
        try {
            var activeWorkspaces = brandWorkspaceRepository.findAll()
                    .stream()
                    .filter(w -> "MONITORING_ACTIVE".equals(w.getStatus()))
                    .toList();

            log.info("DashboardRefreshScheduler - Found {} active workspaces", activeWorkspaces.size());

            for (var workspace : activeWorkspaces) {
                log.info("DashboardRefreshScheduler - Refreshing workspace id={} name={}",
                        workspace.getId(), workspace.getName().value());
                
                var brands = brandRepository.findByWorkspaceId(workspace.getId());
                for (var brand : brands) {
                    mentionIngestionService.ingestForBrand(brand.getId(), brand.getName());
                }
                
                // T-27 DailyMetricsService
                // T-29 IncidentDetectionService
            }

            log.info("DashboardRefreshScheduler - Dashboard refresh completed");
        } catch (Exception e) {
            log.error("DashboardRefreshScheduler - Error during refresh: {}", e.getMessage());
        }
    }
}