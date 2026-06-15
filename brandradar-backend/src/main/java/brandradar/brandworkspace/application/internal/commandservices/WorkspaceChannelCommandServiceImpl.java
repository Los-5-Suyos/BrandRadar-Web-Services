package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.AddWorkspaceChannelCommand;
import brandradar.brandworkspace.application.commandservices.WorkspaceChannelCommandService;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceChannel;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class WorkspaceChannelCommandServiceImpl implements WorkspaceChannelCommandService {

    private final WorkspaceChannelRepository workspaceChannelRepository;

    public WorkspaceChannelCommandServiceImpl(WorkspaceChannelRepository workspaceChannelRepository) {
        this.workspaceChannelRepository = workspaceChannelRepository;
    }

    @Override
    @Transactional
    public Optional<WorkspaceChannel> handle(AddWorkspaceChannelCommand command) {
        if (workspaceChannelRepository.existsByWorkspaceIdAndChannelType(
                command.workspaceId(), command.channelType())) {
            log.warn("Channel {} already exists for workspace {}", command.channelType(), command.workspaceId());
            return Optional.empty();
        }
        var channel = WorkspaceChannel.create(command.workspaceId(), command.channelType());
        var saved = workspaceChannelRepository.save(channel);
        log.info("WorkspaceChannel created with id={}", saved.getId());
        return Optional.of(saved);
    }

    @Override
    @Transactional
    public void deleteByWorkspaceIdAndChannelType(Long workspaceId, String channelType) {
        workspaceChannelRepository.findByWorkspaceIdAndChannelType(workspaceId, channelType)
                .ifPresent(channel -> workspaceChannelRepository.deleteById(channel.getId()));
    }
}