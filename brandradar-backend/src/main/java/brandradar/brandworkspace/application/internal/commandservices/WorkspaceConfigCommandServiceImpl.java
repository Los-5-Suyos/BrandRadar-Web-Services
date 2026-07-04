package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.UpdateWorkspaceConfigCommand;
import brandradar.brandworkspace.application.commandservices.WorkspaceConfigCommandService;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceConfig;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class WorkspaceConfigCommandServiceImpl implements WorkspaceConfigCommandService {

    private final WorkspaceConfigRepository workspaceConfigRepository;

    public WorkspaceConfigCommandServiceImpl(WorkspaceConfigRepository workspaceConfigRepository) {
        this.workspaceConfigRepository = workspaceConfigRepository;
    }

    @Override
    @Transactional
    public WorkspaceConfig handle(UpdateWorkspaceConfigCommand command) {
        var existing = workspaceConfigRepository.findByWorkspaceId(command.workspaceId());

        WorkspaceConfig toSave;
        if (existing.isPresent()) {
            toSave = existing.get().withUpdates(
                    command.companyName(), command.industry(), command.websiteUrl(),
                    command.youtubeUrl(), command.facebookUrl(), command.twitterUrl(),
                    command.tiktokUrl(), command.instagramUrl(), command.redditUrl(),
                    command.googleNewsUrl(), command.logoUrl());
        } else {
            toSave = WorkspaceConfig.create(
                    command.workspaceId(), command.companyName(), command.industry(),
                    command.websiteUrl(), command.youtubeUrl(), command.facebookUrl(),
                    command.twitterUrl(), command.tiktokUrl(), command.instagramUrl(),
                    command.redditUrl(), command.googleNewsUrl(), command.logoUrl());
        }

        var saved = workspaceConfigRepository.save(toSave);
        log.info("WorkspaceConfig upserted for workspaceId={}", command.workspaceId());
        return saved;
    }
}