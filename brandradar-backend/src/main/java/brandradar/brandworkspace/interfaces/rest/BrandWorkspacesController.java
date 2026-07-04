package brandradar.brandworkspace.interfaces.rest;

import brandradar.brandworkspace.application.commandservices.BrandWorkspaceCommandService;
import brandradar.brandworkspace.application.commands.UpdateBrandWorkspaceCommand;
import brandradar.brandworkspace.application.queries.GetWorkspacesByUserIdQuery;
import brandradar.brandworkspace.application.queryservices.BrandWorkspaceQueryService;
import brandradar.brandworkspace.interfaces.rest.resources.BrandWorkspaceResource;
import brandradar.brandworkspace.interfaces.rest.resources.CreateBrandWorkspaceResource;
import brandradar.brandworkspace.interfaces.rest.resources.UpdateBrandWorkspaceResource;
import brandradar.brandworkspace.interfaces.rest.transform.BrandWorkspaceResourceFromEntityAssembler;
import brandradar.brandworkspace.interfaces.rest.transform.CreateBrandWorkspaceCommandFromResourceAssembler;
import brandradar.shared.infrastructure.security.CurrentUser;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/workspaces", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Brand Workspaces", description = "Brand Workspace management endpoints")
public class BrandWorkspacesController {

    private final BrandWorkspaceCommandService commandService;
    private final BrandWorkspaceQueryService queryService;
    private final OwnershipGuard ownershipGuard;
    private final CurrentUser currentUser;

    public BrandWorkspacesController(BrandWorkspaceCommandService commandService,
                                     BrandWorkspaceQueryService queryService,
                                     OwnershipGuard ownershipGuard,
                                     CurrentUser currentUser) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Create a new workspace")
    @PostMapping
    public ResponseEntity<BrandWorkspaceResource> createWorkspace(
            @Valid @RequestBody CreateBrandWorkspaceResource resource) {
        var me = currentUser.get();
        if (!"ADMIN".equals(me.role()) && !me.userId().equals(resource.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create a workspace for another user");
        }
        var command = CreateBrandWorkspaceCommandFromResourceAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(BrandWorkspaceResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get workspace by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BrandWorkspaceResource> getWorkspaceById(@PathVariable Long id) {
        var workspace = ownershipGuard.assertWorkspaceOwnership(id);
        return ResponseEntity.ok(BrandWorkspaceResourceFromEntityAssembler.toResourceFromEntity(workspace));
    }

    @Operation(summary = "Get workspaces by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BrandWorkspaceResource>> getWorkspacesByUserId(@PathVariable Long userId) {
        var me = currentUser.get();
        if (!"ADMIN".equals(me.role()) && !me.userId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot list workspaces of another user");
        }
        var workspaces = queryService.handle(new GetWorkspacesByUserIdQuery(userId));
        var resources = workspaces.stream()
                .map(BrandWorkspaceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Update workspace name and/or plan")
    @PatchMapping("/{id}")
    public ResponseEntity<BrandWorkspaceResource> updateWorkspace(
            @PathVariable Long id,
            @RequestBody UpdateBrandWorkspaceResource resource) {
        ownershipGuard.assertWorkspaceOwnership(id);
        var command = new UpdateBrandWorkspaceCommand(id, resource.name(), resource.plan());
        var updated = commandService.handle(command);
        return ResponseEntity.ok(BrandWorkspaceResourceFromEntityAssembler.toResourceFromEntity(updated));
    }

    @Operation(summary = "Delete a workspace")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        ownershipGuard.assertWorkspaceOwnership(id);
        commandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}