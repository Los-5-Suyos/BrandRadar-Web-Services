package brandradar.iam.interfaces.rest;

import brandradar.iam.application.commands.ForgotPasswordCommand;
import brandradar.iam.application.commands.ResetPasswordCommand;
import brandradar.iam.application.commandservices.UserAccountCommandService;
import brandradar.iam.application.queries.GetAllUserAccountsQuery;
import brandradar.iam.application.queries.GetUserAccountByIdQuery;
import brandradar.iam.application.queryservices.UserAccountQueryService;
import brandradar.iam.interfaces.rest.resources.CreateUserAccountResource;
import brandradar.iam.interfaces.rest.resources.ForgotPasswordResource;
import brandradar.iam.interfaces.rest.resources.ResetPasswordResource;
import brandradar.iam.interfaces.rest.resources.UserAccountResource;
import brandradar.iam.interfaces.rest.transform.CreateUserAccountCommandFromResourceAssembler;
import brandradar.iam.interfaces.rest.transform.UserAccountResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/user-accounts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "User Accounts", description = "IAM - User Account management endpoints")
public class UserAccountController {

    private final UserAccountCommandService userAccountCommandService;
    private final UserAccountQueryService userAccountQueryService;

    public UserAccountController(UserAccountCommandService userAccountCommandService,
                                 UserAccountQueryService userAccountQueryService) {
        this.userAccountCommandService = userAccountCommandService;
        this.userAccountQueryService = userAccountQueryService;
    }

    @Operation(summary = "Create a new user account")
    @PostMapping
    public ResponseEntity<UserAccountResource> createUserAccount(
            @Valid @RequestBody CreateUserAccountResource resource) {
        var command = CreateUserAccountCommandFromResourceAssembler.toCommand(resource);
        var result = userAccountCommandService.handle(command);
        return result
                .map(UserAccountResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @Operation(summary = "Get all user accounts")
    @GetMapping
    public ResponseEntity<List<UserAccountResource>> getAllUserAccounts() {
        var users = userAccountQueryService.handle(new GetAllUserAccountsQuery());
        var resources = users.stream()
                .map(UserAccountResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get user account by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserAccountResource> getUserAccountById(@PathVariable Long id) {
        var result = userAccountQueryService.handle(new GetUserAccountByIdQuery(id));
        return result
                .map(UserAccountResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //T-07
    @Operation(summary = "Request password recovery token")
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordResource resource) {

        // 1. Mapeamos el recurso directamente al comando de aplicación
        var command = new ForgotPasswordCommand(resource.email());

        // 2. Ejecutamos el caso de uso a través del servicio unificado
        userAccountCommandService.handle(command);

        // 3. Retornamos siempre un 200 OK con un mensaje genérico por seguridad
        return ResponseEntity.ok(Map.of("message", "If the email is registered, a recovery link will be sent."));
    }

    //T-08
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordResource resource) {
        // 1. Mapeamos el recurso HTTP entrante al comando interno de la aplicación
        var resetPasswordCommand = new ResetPasswordCommand(resource.token(), resource.newPassword());

        // 2. Ejecutamos el caso de uso a través del Service
        userAccountCommandService.handle(resetPasswordCommand);

        // 3. Retornamos una respuesta clara de éxito
        return ResponseEntity.ok(Map.of("message", "Password has been successfully reset."));
    }
}