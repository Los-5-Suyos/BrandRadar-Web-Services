package brandradar.iam.interfaces.rest;

import brandradar.iam.application.commands.LoginCommand;
import brandradar.iam.application.commandservices.LoginService;
import brandradar.iam.interfaces.rest.resources.LoginRequest;
import brandradar.iam.interfaces.rest.resources.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "IAM - Authentication endpoints")
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Login with email and password. Returns JWT access and refresh tokens.")
    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(request.email(), request.password());
        LoginResponse response = loginService.handle(command);
        return ResponseEntity.ok(response);
    }
}
