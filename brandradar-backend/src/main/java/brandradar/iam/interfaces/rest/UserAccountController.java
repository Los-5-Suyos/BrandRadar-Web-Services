package brandradar.iam.interfaces.rest;

import brandradar.iam.application.commands.UpdateUserProfileCommand;
import brandradar.iam.application.commandservices.UserAccountCommandService;
import brandradar.iam.application.queries.GetAllUserAccountsQuery;
import brandradar.iam.application.queries.GetUserAccountByIdQuery;
import brandradar.iam.application.queryservices.UserAccountQueryService;
import brandradar.iam.infrastructure.storage.AvatarStorageService;
import brandradar.iam.interfaces.rest.resources.ChangePasswordResource;
import brandradar.iam.interfaces.rest.resources.CreateUserAccountResource;
import brandradar.iam.interfaces.rest.resources.UpdateUserProfileResource;
import brandradar.iam.interfaces.rest.resources.UserAccountResource;
import brandradar.iam.interfaces.rest.transform.CreateUserAccountCommandFromResourceAssembler;
import brandradar.iam.interfaces.rest.transform.UserAccountResourceFromEntityAssembler;
import brandradar.shared.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/user-accounts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "User Accounts", description = "IAM - User Account management endpoints")
public class UserAccountController {

    private final UserAccountCommandService userAccountCommandService;
    private final UserAccountQueryService userAccountQueryService;
    private final CurrentUser currentUser;
    private final AvatarStorageService avatarStorageService;

    public UserAccountController(UserAccountCommandService userAccountCommandService,
                                 UserAccountQueryService userAccountQueryService,
                                 CurrentUser currentUser,
                                 AvatarStorageService avatarStorageService) {
        this.userAccountCommandService = userAccountCommandService;
        this.userAccountQueryService = userAccountQueryService;
        this.currentUser = currentUser;
        this.avatarStorageService = avatarStorageService;
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

    @Operation(summary = "Get all user accounts (ADMIN only)")
    @GetMapping
    public ResponseEntity<List<UserAccountResource>> getAllUserAccounts() {
        if (!"ADMIN".equals(currentUser.get().role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
        var users = userAccountQueryService.handle(new GetAllUserAccountsQuery());
        var resources = users.stream()
                .map(UserAccountResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get user account by ID (self or ADMIN)")
    @GetMapping("/{id}")
    public ResponseEntity<UserAccountResource> getUserAccountById(@PathVariable Long id) {
        assertSelfOrAdmin(id);
        var result = userAccountQueryService.handle(new GetUserAccountByIdQuery(id));
        return result
                .map(UserAccountResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update own profile (fullName, bio, language, timezone, emailNotifications)")
    @PatchMapping("/{id}")
    public ResponseEntity<UserAccountResource> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateUserProfileResource resource) {
        assertSelfOrAdmin(id);
        var command = new UpdateUserProfileCommand(id, resource.fullName(), resource.bio(),
                resource.language(), resource.timezone(), resource.emailNotifications(), null);
        var updated = userAccountCommandService.handle(command);
        return ResponseEntity.ok(UserAccountResourceFromEntityAssembler.toResourceFromEntity(updated));
    }

    @Operation(summary = "Upload/replace the user's avatar (png/jpeg/webp, máx. 5MB)")
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserAccountResource> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        assertSelfOrAdmin(id);
        var avatarUrl = avatarStorageService.store(file, id);

        var command = new UpdateUserProfileCommand(id, null, null, null, null, null, avatarUrl);
        var updated = userAccountCommandService.handle(command);
        return ResponseEntity.ok(UserAccountResourceFromEntityAssembler.toResourceFromEntity(updated));
    }

    @Operation(summary = "Change own password (requires current password)")
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordResource resource) {
        var me = currentUser.get();
        if (!me.userId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot change another user's password");
        }
        userAccountCommandService.changePassword(id, resource.currentPassword(), resource.newPassword());
        return ResponseEntity.ok().build();
    }

    private void assertSelfOrAdmin(Long id) {
        var me = currentUser.get();
        if (!"ADMIN".equals(me.role()) && !me.userId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot access another user's account");
        }
    }
}