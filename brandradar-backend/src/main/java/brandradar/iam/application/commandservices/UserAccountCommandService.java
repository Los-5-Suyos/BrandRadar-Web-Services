package brandradar.iam.application.commandservices;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.application.commands.UpdateUserProfileCommand;
import brandradar.iam.application.commands.VerifyAccountCommand;
import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.valueobjects.PasswordHash;

import java.util.Optional;

public interface UserAccountCommandService {
    Optional<UserAccount> handle(CreateUserAccountCommand command);
    Optional<UserAccount> verify(VerifyAccountCommand command);
    UserAccount handle(UpdateUserProfileCommand command);
    void updatePasswordHash(Long userId, PasswordHash newHash);
    void changePassword(Long userId, String currentPassword, String newPassword);
}