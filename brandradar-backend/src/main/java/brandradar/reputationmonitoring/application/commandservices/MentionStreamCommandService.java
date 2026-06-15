package brandradar.reputationmonitoring.application.commandservices;

import brandradar.reputationmonitoring.application.commands.CreateMentionStreamCommand;
import brandradar.reputationmonitoring.domain.model.aggregates.MentionStream;

import java.util.Optional;

public interface MentionStreamCommandService {
    Optional<MentionStream> handle(CreateMentionStreamCommand command);
}