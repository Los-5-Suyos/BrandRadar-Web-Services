package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.CreateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commands.UpdateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commandservices.BrandWorkspaceCommandService;
import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.iam.domain.model.repositories.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class BrandWorkspaceCommandServiceImpl implements BrandWorkspaceCommandService {

    private static final Map<String, Integer> MAX_WORKSPACES_BY_ROLE = Map.of(
            "PYME", 1,
            "AGENCIA", 2,
            "ADMIN", Integer.MAX_VALUE
    );

    private final BrandWorkspaceRepository brandWorkspaceRepository;
    private final UserAccountRepository userAccountRepository;
    private final WorkspaceCascadeDeletionService cascadeDeletionService;

    public BrandWorkspaceCommandServiceImpl(BrandWorkspaceRepository brandWorkspaceRepository,
                                            UserAccountRepository userAccountRepository,
                                            WorkspaceCascadeDeletionService cascadeDeletionService) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
        this.userAccountRepository = userAccountRepository;
        this.cascadeDeletionService = cascadeDeletionService;
    }

    @Override
    @Transactional
    public Optional<BrandWorkspace> handle(CreateBrandWorkspaceCommand command) {
        var user = userAccountRepository.findById(command.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        int max = MAX_WORKSPACES_BY_ROLE.getOrDefault(user.getRole(), 1);
        int current = brandWorkspaceRepository.findByUserId(command.userId()).size();
        if (current >= max) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Plan limit reached: role " + user.getRole() + " allows at most " + max + " workspace(s)");
        }

        var workspace = BrandWorkspace.create(command.userId(), command.name(), command.plan());
        var saved = brandWorkspaceRepository.save(workspace);
        log.info("BrandWorkspace created with id={}", saved.getId());
        return Optional.of(saved);
    }

    @Override
    @Transactional
    public BrandWorkspace handle(UpdateBrandWorkspaceCommand command) {
        var existing = brandWorkspaceRepository.findById(command.workspaceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found"));

        var updated = existing.withUpdates(command.name(), command.plan());
        var saved = brandWorkspaceRepository.save(updated);
        log.info("BrandWorkspace updated with id={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public void deleteById(Long workspaceId) {
        cascadeDeletionService.deleteWorkspaceCascade(workspaceId);
        brandWorkspaceRepository.deleteById(workspaceId);
        log.info("BrandWorkspace and all related data deleted for id={}", workspaceId);
    }
}