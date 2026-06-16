package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.CreateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commandservices.BrandWorkspaceCommandService;
import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceStatus;
import brandradar.shared.exception.DomainValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class BrandWorkspaceCommandServiceImpl implements BrandWorkspaceCommandService {

    private static final int MAX_ACTIVE_WORKSPACES = 3;

    private final BrandWorkspaceRepository brandWorkspaceRepository;

    public BrandWorkspaceCommandServiceImpl(BrandWorkspaceRepository brandWorkspaceRepository) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
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
}
