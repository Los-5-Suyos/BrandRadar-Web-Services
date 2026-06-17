package brandradar.iam.application.internal.commandservices;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.application.commands.ForgotPasswordCommand;
import brandradar.iam.application.commandservices.UserAccountCommandService;
import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.repositories.UserAccountRepository;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import brandradar.iam.application.commands.ResetPasswordCommand;
import brandradar.iam.domain.model.valueobjects.PasswordHash;

import java.util.Optional;

@Slf4j
@Service
public class UserAccountCommandServiceImpl implements UserAccountCommandService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountCommandServiceImpl(UserAccountRepository userAccountRepository,
                                         PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Optional<UserAccount> handle(CreateUserAccountCommand command) {
        if (userAccountRepository.existsByEmail(command.email())) {
            log.warn("User with email {} already exists", command.email().value());
            return Optional.empty();
        }
        var hashedPassword = new PasswordHash(passwordEncoder.encode(command.passwordHash().value()));
        var userAccount = UserAccount.create(
                command.email(),
                hashedPassword,
                command.role(),
                command.description()
        );
        var saved = userAccountRepository.save(userAccount);
        log.info("UserAccount created with id={}", saved.getId());
        return Optional.of(saved);
    }

    @Override
    @Transactional
    public void handle(ForgotPasswordCommand command) {
        brandradar.iam.domain.model.valueobjects.Email emailVo =
                new brandradar.iam.domain.model.valueobjects.Email(command.email());

        java.util.Optional<brandradar.iam.domain.model.aggregates.UserAccount> userAccountOptional =
                userAccountRepository.findByEmail(emailVo);

        if (userAccountOptional.isPresent()) {
            brandradar.iam.domain.model.aggregates.UserAccount userAccount = userAccountOptional.get();

            String secureToken = java.util.UUID.randomUUID().toString();

            brandradar.iam.domain.model.aggregates.UserAccount updatedUser =
                    userAccount.withPasswordRecoveryToken(secureToken);

            userAccountRepository.save(updatedUser);

            System.out.println("Token generado con éxito para " + command.email() + " -> " + secureToken);
        } else {
            System.out.println("Intento de recuperación para correo no registrado: " + command.email());
        }
    }

    @Override
    public void handle(ResetPasswordCommand command) {
        var userAccountOptional = userAccountRepository.findByPasswordRecoveryToken(command.token());

        if (userAccountOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid recovery token.");
        }

        var userAccount = userAccountOptional.get();

        if (userAccount.getTokenExpiryDate().isBefore(java.time.Instant.now())) {
            throw new IllegalStateException("Recovery token has expired.");
        }

        var newPasswordHash = new PasswordHash(command.newPassword());

        var updatedUserAccount = userAccount.withUpdatedPassword(newPasswordHash);

        userAccountRepository.save(updatedUserAccount);
    }
}