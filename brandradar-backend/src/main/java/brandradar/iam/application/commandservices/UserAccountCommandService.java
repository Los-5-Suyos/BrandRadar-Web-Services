package brandradar.iam.application.commandservices;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.domain.model.aggregates.UserAccount;

import java.util.Optional;

public interface UserAccountCommandService {
    Optional<UserAccount> handle(CreateUserAccountCommand command);
}