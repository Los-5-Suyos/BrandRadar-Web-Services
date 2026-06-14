package brandradar.iam.application.internal.commandservices;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.application.commandservices.UserAccountCommandService;
import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.repositories.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserAccountCommandServiceImpl implements UserAccountCommandService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountCommandServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional
    public Optional<UserAccount> handle(CreateUserAccountCommand command) {
        if (userAccountRepository.existsByEmail(command.email())) {
            log.warn("User with email already exists");
            return Optional.empty();
        }
        var userAccount = UserAccount.create(
                command.email(),
                command.passwordHash(),
                command.role(),
                command.description()
        );
        var saved = userAccountRepository.save(userAccount);
        log.info("UserAccount created with id={}", saved.getId());
        return Optional.of(saved);
    }
}