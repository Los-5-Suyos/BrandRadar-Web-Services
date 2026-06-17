package brandradar.iam.application.internal.commandservices;

import brandradar.iam.application.commands.LoginCommand;
import brandradar.iam.application.commandservices.LoginService;
import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.repositories.UserAccountRepository;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.infrastructure.security.jwt.JwtTokenProvider;
import brandradar.iam.interfaces.rest.resources.LoginResponse;
import brandradar.shared.exceptions.DomainValidationException;
import brandradar.shared.exceptions.UnauthorizedWorkspaceAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    // Expiración del refresh token: 7 días en milisegundos
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 604_800_000L;
    // Expiración del access token en segundos (para la respuesta)
    private static final int ACCESS_TOKEN_EXPIRES_IN_SECONDS = 900;

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginServiceImpl(UserAccountRepository userAccountRepository,
                            PasswordEncoder passwordEncoder,
                            JwtTokenProvider jwtTokenProvider) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public LoginResponse handle(LoginCommand command) {
        // 1. Buscar cuenta por email
        UserAccount account = userAccountRepository
                .findByEmail(new Email(command.email()))
                .orElseThrow(() -> new DomainValidationException("Invalid credentials"));

        // 2. Verificar estado de la cuenta
        if (account.isBlocked()) {
            log.warn("Login attempt on blocked account: {}", command.email());
            throw new UnauthorizedWorkspaceAccessException("Account is blocked due to too many failed login attempts");
        }

        if (account.isPendingVerification()) {
            log.warn("Login attempt on unverified account: {}", command.email());
            throw new DomainValidationException("Account is not verified. Please check your email.");
        }

        // 3. Verificar contraseña con BCrypt
        if (!passwordEncoder.matches(command.password(), account.getPasswordHash().value())) {
            account.incrementFailedAttempts();
            userAccountRepository.save(account);
            log.warn("Failed login attempt for: {}. Attempts: {}", command.email(), account.getFailedLoginAttempts());

            if (account.isBlocked()) {
                throw new UnauthorizedWorkspaceAccessException("Account has been blocked after 5 failed attempts");
            }
            throw new DomainValidationException("Invalid credentials");
        }

        // 4. Login exitoso: resetear intentos y generar tokens
        account.resetFailedAttempts();
        userAccountRepository.save(account);
        log.info("Successful login for: {}", command.email());

        String accessToken = jwtTokenProvider.generateToken(account.getEmail().value(), account.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(account.getEmail().value(), account.getId());

        return new LoginResponse(accessToken, refreshToken, ACCESS_TOKEN_EXPIRES_IN_SECONDS);
    }
}
