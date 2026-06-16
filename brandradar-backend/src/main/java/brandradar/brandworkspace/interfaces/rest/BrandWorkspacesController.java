package brandradar.brandworkspace.interfaces.rest;

import brandradar.brandworkspace.application.commandservices.BrandWorkspaceCommandService;
import brandradar.brandworkspace.application.queries.GetWorkspaceByIdQuery;
import brandradar.brandworkspace.application.queries.GetWorkspacesByUserIdQuery;
import brandradar.brandworkspace.application.queryservices.BrandWorkspaceQueryService;
import brandradar.brandworkspace.interfaces.rest.resources.BrandWorkspaceResource;
import brandradar.brandworkspace.interfaces.rest.resources.CreateBrandWorkspaceResource;
import brandradar.brandworkspace.interfaces.rest.transform.BrandWorkspaceResourceFromEntityAssembler;
import brandradar.brandworkspace.interfaces.rest.transform.CreateBrandWorkspaceCommandFromResourceAssembler;
import brandradar.shared.exception.DomainValidationException;
import brandradar.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/workspaces", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Brand Workspaces", description = "Brand Workspace management endpoints")
public class BrandWorkspacesController {

    private final BrandWorkspaceCommandService commandService;
    private final BrandWorkspaceQueryService queryService;

    public BrandWorkspacesController(BrandWorkspaceCommandService commandService,
                                     BrandWorkspaceQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a new workspace for the authenticated user")
    @PostMapping
    public ResponseEntity<BrandWorkspaceResource> createWorkspace(
            @Valid @RequestBody CreateBrandWorkspaceResource resource) {
        var userId = AuthenticatedUser.getId();
        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var command = CreateBrandWorkspaceCommandFromResourceAssembler.toCommand(resource, userId.get());
        try {
            var result = commandService.handle(command);
            return result
                    .map(BrandWorkspaceResourceFromEntityAssembler::toResourceFromEntity)
                    .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                    .orElse(ResponseEntity.badRequest().build());
        } catch (DomainValidationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get a workspace owned by the authenticated user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BrandWorkspaceResource> getWorkspaceById(@PathVariable Long id) {
        var userId = AuthenticatedUser.getId();
        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var result = queryService.handle(new GetWorkspaceByIdQuery(id, userId.get()));
        return result
                .map(BrandWorkspaceResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @Operation(summary = "Get the active workspaces of the authenticated user")
    @GetMapping
    public ResponseEntity<List<BrandWorkspaceResource>> getWorkspaces() {
        var userId = AuthenticatedUser.getId();
        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var workspaces = queryService.handle(new GetWorkspacesByUserIdQuery(userId.get()));
        var resources = workspaces.stream()
                .map(BrandWorkspaceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}