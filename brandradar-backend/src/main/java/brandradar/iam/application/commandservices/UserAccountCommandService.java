package brandradar.iam.application.commandservices;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.application.commands.ForgotPasswordCommand;
import brandradar.iam.application.commands.ResetPasswordCommand;
import brandradar.iam.domain.model.aggregates.UserAccount;

import java.util.Optional;

public interface UserAccountCommandService {
    Optional<UserAccount> handle(CreateUserAccountCommand command);
    void handle(ForgotPasswordCommand command);
    void handle(ResetPasswordCommand command);
}