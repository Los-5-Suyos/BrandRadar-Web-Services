package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.AddWorkspaceChannelCommand;
import brandradar.brandworkspace.application.commandservices.WorkspaceChannelCommandService;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceChannel;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import brandradar.brandworkspace.domain.model.services.ChannelPlanPolicy;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class WorkspaceChannelCommandServiceImpl implements WorkspaceChannelCommandService {

    private final WorkspaceChannelRepository workspaceChannelRepository;
    private final BrandWorkspaceRepository brandWorkspaceRepository;

    public WorkspaceChannelCommandServiceImpl(WorkspaceChannelRepository workspaceChannelRepository,
                                              BrandWorkspaceRepository brandWorkspaceRepository) {
        this.workspaceChannelRepository = workspaceChannelRepository;
        this.brandWorkspaceRepository = brandWorkspaceRepository;
    }

    @Override
    @Transactional
    public Optional<WorkspaceChannel> handle(AddWorkspaceChannelCommand command) {
        var workspace = brandWorkspaceRepository.findById(command.workspaceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found"));

        var allowedChannels = ChannelPlanPolicy.allowedFor(workspace.getPlan());
        if (!allowedChannels.contains(command.channelType())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Channel " + command.channelType() + " is not available on plan " + workspace.getPlan());
        }

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