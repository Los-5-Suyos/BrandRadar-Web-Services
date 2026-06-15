package brandradar.reputationmonitoring.application.commandservices;

import brandradar.reputationmonitoring.application.commands.CreateMentionCommand;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;

import java.util.Optional;

public interface MentionCommandService {
    Optional<Mention> handle(CreateMentionCommand command);
}