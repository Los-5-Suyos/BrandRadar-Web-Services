package brandradar.iam.application.internal.commandservices;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.application.commands.UpdateUserProfileCommand;
import brandradar.iam.application.commands.VerifyAccountCommand;
import brandradar.iam.application.commandservices.UserAccountCommandService;
import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.repositories.UserAccountRepository;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public Optional<UserAccount> verify(VerifyAccountCommand command) {
        var userOpt = userAccountRepository.findByEmail(command.email());
        if (userOpt.isEmpty()) return Optional.empty();
        var user = userOpt.get();
        if (!user.getVerificationCode().equals(command.code())) return Optional.empty();
        var activated = user.activate();
        return Optional.of(userAccountRepository.save(activated));
    }

    @Override
    @Transactional
    public UserAccount handle(UpdateUserProfileCommand command) {
        var user = userAccountRepository.findById(command.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var updated = user.withProfileUpdates(command.fullName(), command.bio(), command.language(),
                command.timezone(), command.emailNotifications(), command.avatarUrl());
        var saved = userAccountRepository.save(updated);
        log.info("UserAccount profile updated for id={}", command.userId());
        return saved;
    }

    @Override
    @Transactional
    public void updatePasswordHash(Long userId, PasswordHash newHash) {
        var user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userAccountRepository.save(user.withNewPasswordHash(newHash));
        log.info("Password reset for userId={}", userId);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        var user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash().value())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        var newHash = new PasswordHash(passwordEncoder.encode(newPassword));
        userAccountRepository.save(user.withNewPasswordHash(newHash));
        log.info("Password changed by user for userId={}", userId);
    }
}