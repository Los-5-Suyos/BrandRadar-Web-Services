package brandradar.iam.interfaces.rest;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.application.commandservices.UserAccountCommandService;
import brandradar.iam.application.queryservices.UserAccountQueryService;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import brandradar.iam.interfaces.rest.resources.LoginRequest;
import brandradar.iam.interfaces.rest.resources.LoginResponse;
import brandradar.iam.interfaces.rest.resources.RegisterUserResource;
import brandradar.iam.interfaces.rest.resources.RegisteredUserResource;
import brandradar.shared.infrastructure.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Auth endpoints - register, login")
public class AuthController {

    private final UserAccountCommandService userAccountCommandService;
    private final UserAccountQueryService userAccountQueryService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserAccountCommandService userAccountCommandService,
                          UserAccountQueryService userAccountQueryService,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.userAccountCommandService = userAccountCommandService;
        this.userAccountQueryService = userAccountQueryService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
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

        if (!"ACTIVE".equals(user.getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String token = jwtTokenProvider.generateAccessToken(user.getEmail().value(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail().value());

        return ResponseEntity.ok(new LoginResponse(
                token,
                refreshToken,
                user.getId(),
                user.getEmail().value(),
                user.getRole()
        ));
    }
}