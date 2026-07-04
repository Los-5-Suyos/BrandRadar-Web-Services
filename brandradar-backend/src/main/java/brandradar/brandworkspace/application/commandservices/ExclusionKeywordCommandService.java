package brandradar.brandworkspace.application.commandservices;

import brandradar.brandworkspace.application.commands.CreateExclusionKeywordCommand;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceExclusionKeyword;

public interface ExclusionKeywordCommandService {
    WorkspaceExclusionKeyword handle(CreateExclusionKeywordCommand command);
    void deleteById(Long id);
}