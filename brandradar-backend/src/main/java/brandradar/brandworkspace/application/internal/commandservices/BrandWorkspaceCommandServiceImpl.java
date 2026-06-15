package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.CreateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.commandservices.BrandWorkspaceCommandService;
import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class BrandWorkspaceCommandServiceImpl implements BrandWorkspaceCommandService {

    private final BrandWorkspaceRepository brandWorkspaceRepository;

    public BrandWorkspaceCommandServiceImpl(BrandWorkspaceRepository brandWorkspaceRepository) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
    }

    @Override
    @Transactional
    public Optional<BrandWorkspace> handle(CreateBrandWorkspaceCommand command) {
        var workspace = BrandWorkspace.create(command.userId(), command.name(), command.plan());
        var saved = brandWorkspaceRepository.save(workspace);
        log.info("BrandWorkspace created with id={}", saved.getId());
        return Optional.of(saved);
    }
}