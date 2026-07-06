package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.domain.model.services.ChannelPlanPolicy;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.KeywordRuleRepository;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceChannelRepository;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceExclusionKeywordRepository;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import brandradar.crisisdetection.domain.model.repositories.MonitoringRuleRepository;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.reputationmonitoring.domain.model.repositories.MentionStreamRepository;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationIncidentRepository;
import brandradar.sentimentintelligence.domain.model.repositories.SentimentAnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class WorkspaceCascadeDeletionService {

    private final BrandRepository brandRepository;
    private final WorkspaceChannelRepository workspaceChannelRepository;
    private final WorkspaceExclusionKeywordRepository exclusionKeywordRepository;
    private final KeywordRuleRepository keywordRuleRepository;
    private final MentionRepository mentionRepository;
    private final MentionStreamRepository mentionStreamRepository;
    private final ReputationIncidentRepository reputationIncidentRepository;
    private final CrisisAlertRepository crisisAlertRepository;
    private final MonitoringRuleRepository monitoringRuleRepository;
    private final SentimentAnalysisRepository sentimentAnalysisRepository;

    public WorkspaceCascadeDeletionService(BrandRepository brandRepository,
                                           WorkspaceChannelRepository workspaceChannelRepository,
                                           WorkspaceExclusionKeywordRepository exclusionKeywordRepository,
                                           KeywordRuleRepository keywordRuleRepository,
                                           MentionRepository mentionRepository,
                                           MentionStreamRepository mentionStreamRepository,
                                           ReputationIncidentRepository reputationIncidentRepository,
                                           CrisisAlertRepository crisisAlertRepository,
                                           MonitoringRuleRepository monitoringRuleRepository,
                                           SentimentAnalysisRepository sentimentAnalysisRepository) {
        this.brandRepository = brandRepository;
        this.workspaceChannelRepository = workspaceChannelRepository;
        this.exclusionKeywordRepository = exclusionKeywordRepository;
        this.keywordRuleRepository = keywordRuleRepository;
        this.mentionRepository = mentionRepository;
        this.mentionStreamRepository = mentionStreamRepository;
        this.reputationIncidentRepository = reputationIncidentRepository;
        this.crisisAlertRepository = crisisAlertRepository;
        this.monitoringRuleRepository = monitoringRuleRepository;
        this.sentimentAnalysisRepository = sentimentAnalysisRepository;
    }

    @Transactional
    public void deleteWorkspaceCascade(Long workspaceId) {
        var brands = brandRepository.findByWorkspaceId(workspaceId);
        log.info("WorkspaceCascadeDeletionService - Deleting workspace {} with {} brand(s)",
                workspaceId, brands.size());

        for (var brand : brands) {
            var brandId = brand.getId();

            mentionRepository.findByBrandId(brandId)
                    .forEach(m -> mentionRepository.deleteById(m.getId()));

            mentionStreamRepository.findByBrandId(brandId)
                    .forEach(ms -> mentionStreamRepository.deleteById(ms.getId()));

            reputationIncidentRepository.findByBrandId(brandId)
                    .forEach(ri -> reputationIncidentRepository.deleteById(ri.getId()));

            crisisAlertRepository.findByBrandId(brandId)
                    .forEach(ca -> crisisAlertRepository.deleteById(ca.getId()));

            monitoringRuleRepository.findByBrandId(brandId)
                    .forEach(mr -> monitoringRuleRepository.deleteById(mr.getId()));

            sentimentAnalysisRepository.findByBrandId(brandId)
                    .forEach(sa -> sentimentAnalysisRepository.deleteById(sa.getId()));

            keywordRuleRepository.findByBrandId(brandId)
                    .forEach(kr -> keywordRuleRepository.deleteById(kr.getId()));

            brandRepository.deleteById(brandId);
            log.info("WorkspaceCascadeDeletionService - Brand {} and its data deleted", brandId);
        }

        workspaceChannelRepository.findByWorkspaceId(workspaceId)
                .forEach(ch -> workspaceChannelRepository.deleteById(ch.getId()));

        exclusionKeywordRepository.findByWorkspaceId(workspaceId)
                .forEach(ek -> exclusionKeywordRepository.deleteById(ek.getId()));

        log.info("WorkspaceCascadeDeletionService - Workspace {} fully cleaned up", workspaceId);
    }
}