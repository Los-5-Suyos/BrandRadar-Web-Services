package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.CreateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commands.DeactivateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commands.UpdateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commandservices.BrandWorkspaceCommandService;
import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.events.WorkspaceDeactivatedEvent;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceStatus;
import brandradar.shared.exception.DomainValidationException;
import brandradar.shared.exception.ResourceNotFoundException;
import brandradar.shared.exception.UnauthorizedWorkspaceAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class BrandWorkspaceCommandServiceImpl implements BrandWorkspaceCommandService {

    private static final int MAX_ACTIVE_WORKSPACES = 3;

    private final BrandWorkspaceRepository brandWorkspaceRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BrandWorkspaceCommandServiceImpl(BrandWorkspaceRepository brandWorkspaceRepository,
                                            ApplicationEventPublisher eventPublisher) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Optional<BrandWorkspace> handle(CreateBrandWorkspaceCommand command) {
        var activeWorkspaceCount = brandWorkspaceRepository.findByUserId(command.userId())
                .stream()
                .filter(workspace -> workspace.getStatus() == WorkspaceStatus.ACTIVO)
                .count();
        if (activeWorkspaceCount >= MAX_ACTIVE_WORKSPACES) {
            throw new DomainValidationException(
                    "User already has the maximum of " + MAX_ACTIVE_WORKSPACES + " active workspaces");
        }

        var workspace = BrandWorkspace.create(command.userId(), command.name(), command.description());
        var saved = brandWorkspaceRepository.save(workspace);
        log.info("BrandWorkspace created with id={}", saved.getId());
        return Optional.of(saved);
    }

    @Override
    @Transactional
    public BrandWorkspace handle(UpdateBrandWorkspaceCommand command) {
        var existing = brandWorkspaceRepository.findById(command.id())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace " + command.id() + " not found"));

        if (!existing.getUserId().equals(command.userId())) {
            throw new UnauthorizedWorkspaceAccessException(
                    "User " + command.userId() + " does not own workspace " + command.id());
        }

        if (existing.getStatus() != WorkspaceStatus.ACTIVO) {
            throw new DomainValidationException("Only ACTIVO workspaces can be updated");
        }

        var updated = BrandWorkspace.rehydrate(existing.getId(), existing.getUserId(), command.name(),
                command.description(), existing.getStatus(), existing.getCreatedAt(), existing.getUpdatedAt());
        var saved = brandWorkspaceRepository.save(updated);
        log.info("BrandWorkspace updated with id={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public void handle(DeactivateBrandWorkspaceCommand command) {
        var existing = brandWorkspaceRepository.findById(command.id())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace " + command.id() + " not found"));

        if (!existing.getUserId().equals(command.userId())) {
            throw new UnauthorizedWorkspaceAccessException(
                    "User " + command.userId() + " does not own workspace " + command.id());
        }

        var deactivated = BrandWorkspace.rehydrate(existing.getId(), existing.getUserId(), existing.getName(),
                existing.getDescription(), WorkspaceStatus.INACTIVO, existing.getCreatedAt(), existing.getUpdatedAt());
        brandWorkspaceRepository.save(deactivated);
        log.info("BrandWorkspace deactivated with id={}", existing.getId());

        eventPublisher.publishEvent(new WorkspaceDeactivatedEvent(existing.getId(), existing.getUserId()));
    }
}
