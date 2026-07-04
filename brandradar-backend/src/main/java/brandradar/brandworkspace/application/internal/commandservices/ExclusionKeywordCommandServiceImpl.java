package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.CreateExclusionKeywordCommand;
import brandradar.brandworkspace.application.commandservices.ExclusionKeywordCommandService;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceExclusionKeyword;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceExclusionKeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ExclusionKeywordCommandServiceImpl implements ExclusionKeywordCommandService {

    private final WorkspaceExclusionKeywordRepository exclusionKeywordRepository;

    public ExclusionKeywordCommandServiceImpl(WorkspaceExclusionKeywordRepository exclusionKeywordRepository) {
        this.exclusionKeywordRepository = exclusionKeywordRepository;
    }

    @Override
    @Transactional
    public WorkspaceExclusionKeyword handle(CreateExclusionKeywordCommand command) {
        var keyword = WorkspaceExclusionKeyword.create(command.workspaceId(), command.keyword());
        var saved = exclusionKeywordRepository.save(keyword);
        log.info("ExclusionKeyword created with id={} for workspaceId={}", saved.getId(), command.workspaceId());
        return saved;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        exclusionKeywordRepository.deleteById(id);
        log.info("ExclusionKeyword deleted with id={}", id);
    }
}