package brandradar.iam.interfaces.rest;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.application.commandservices.UserAccountCommandService;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import brandradar.iam.interfaces.rest.resources.RegisterUserResource;
import brandradar.iam.interfaces.rest.resources.RegisteredUserResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Auth endpoints - register, login, verify")
public class AuthController {

    private final UserAccountCommandService userAccountCommandService;

    public AuthController(UserAccountCommandService userAccountCommandService) {
        this.userAccountCommandService = userAccountCommandService;
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
}