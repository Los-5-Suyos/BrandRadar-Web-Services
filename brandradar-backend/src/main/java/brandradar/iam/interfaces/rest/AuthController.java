package brandradar.iam.interfaces.rest;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.application.commands.VerifyAccountCommand;
import brandradar.iam.application.commandservices.UserAccountCommandService;
import brandradar.iam.application.queries.GetUserAccountByIdQuery;
import brandradar.iam.application.queryservices.UserAccountQueryService;
import brandradar.iam.domain.model.aggregates.PasswordRecovery;
import brandradar.iam.domain.model.repositories.PasswordRecoveryRepository;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import brandradar.iam.interfaces.rest.resources.ForgotPasswordRequest;
import brandradar.iam.interfaces.rest.resources.LoginRequest;
import brandradar.iam.interfaces.rest.resources.LoginResponse;
import brandradar.iam.interfaces.rest.resources.RefreshTokenRequest;
import brandradar.iam.interfaces.rest.resources.RegisterUserResource;
import brandradar.iam.interfaces.rest.resources.RegisteredUserResource;
import brandradar.iam.interfaces.rest.resources.ResetPasswordRequest;
import brandradar.shared.infrastructure.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Auth endpoints - register, login")
public class AuthController {

    private final UserAccountCommandService userAccountCommandService;
    private final UserAccountQueryService userAccountQueryService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordRecoveryRepository passwordRecoveryRepository;

    public AuthController(UserAccountCommandService userAccountCommandService,
                          UserAccountQueryService userAccountQueryService,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordRecoveryRepository passwordRecoveryRepository) {
        this.userAccountCommandService = userAccountCommandService;
        this.userAccountQueryService = userAccountQueryService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordRecoveryRepository = passwordRecoveryRepository;
    }

    @Operation(summary = "Register a new user account")
    @PostMapping("/register")
    public ResponseEntity<RegisteredUserResource> register(
            @Valid @RequestBody RegisterUserResource resource) {
        var command = new CreateUserAccountCommand(
                new Email(resource.email()),
                new PasswordHash(resource.password()),
                resource.role(),
                resource.description()
        );
        var result = userAccountCommandService.handle(command);
        return result
                .map(user -> new RegisteredUserResource(
                        user.getId(),
                        user.getEmail().value(),
                        user.getRole(),
                        user.getStatus(),
                        user.getCreatedAt()
                ))
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @Operation(summary = "Login with email and password")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        var userOpt = userAccountQueryService.findByEmail(new Email(request.email()));

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userOpt.get();

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash().value())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if ("BLOCKED".equals(user.getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String token = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail().value(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail().value());

        return ResponseEntity.ok(new LoginResponse(
                token,
                refreshToken,
                user.getId(),
                user.getEmail().value(),
                user.getRole()
        ));
    }

    @Operation(summary = "Exchange a valid refresh token for a new access token")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        var refreshToken = request.refreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)
                || !"refresh".equals(jwtTokenProvider.getTypeFromToken(refreshToken))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var email = jwtTokenProvider.getEmailFromToken(refreshToken);
        var userOpt = userAccountQueryService.findByEmail(new Email(email));
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var user = userOpt.get();

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail().value(), user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail().value());

        return ResponseEntity.ok(new LoginResponse(
                newAccessToken,
                newRefreshToken,
                user.getId(),
                user.getEmail().value(),
                user.getRole()
        ));
    }

    @Operation(summary = "Request a password reset token. Always returns 200 regardless of " +
            "whether the email exists, to avoid leaking which emails are registered. " +
            "Since there's no email service configured yet, the token is logged to the console " +
            "instead of being sent — check the backend logs to get it during development.")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        var userOpt = userAccountQueryService.findByEmail(new Email(request.email()));

        if (userOpt.isPresent()) {
            var user = userOpt.get();
            var token = UUID.randomUUID().toString();
            var expiresAt = Instant.now().plus(1, ChronoUnit.HOURS);
            var recovery = PasswordRecovery.create(user.getId(), token, expiresAt);
            passwordRecoveryRepository.save(recovery);

            log.info("[PASSWORD RESET] Token for {} (expira en 1h): {}", request.email(), token);
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset the password using a valid, non-expired token")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        var recoveryOpt = passwordRecoveryRepository.findByToken(request.token());

        if (recoveryOpt.isEmpty() || !recoveryOpt.get().isUsable()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var recovery = recoveryOpt.get();

        var userOpt = userAccountQueryService.handle(new GetUserAccountByIdQuery(recovery.getUserId()));
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var newHash = new PasswordHash(passwordEncoder.encode(request.newPassword()));
        userAccountCommandService.updatePasswordHash(userOpt.get().getId(), newHash);

        passwordRecoveryRepository.save(recovery.markUsed());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Verify account with code")
    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam String email, @RequestParam String code) {
        var command = new VerifyAccountCommand(new Email(email), code);
        var result = userAccountCommandService.verify(command);
        return result.isPresent()
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}