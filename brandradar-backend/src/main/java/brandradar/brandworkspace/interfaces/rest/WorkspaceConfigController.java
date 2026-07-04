package brandradar.brandworkspace.interfaces.rest;

import brandradar.brandworkspace.application.commands.UpdateWorkspaceConfigCommand;
import brandradar.brandworkspace.application.commandservices.WorkspaceConfigCommandService;
import brandradar.brandworkspace.application.queries.GetWorkspaceConfigByWorkspaceIdQuery;
import brandradar.brandworkspace.application.queryservices.WorkspaceConfigQueryService;
import brandradar.brandworkspace.infrastructure.storage.LogoStorageService;
import brandradar.brandworkspace.interfaces.rest.resources.UpdateWorkspaceConfigResource;
import brandradar.brandworkspace.interfaces.rest.resources.WorkspaceConfigResource;
import brandradar.brandworkspace.interfaces.rest.transform.WorkspaceConfigAssembler;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/workspaces/{workspaceId}/config", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Workspace Config", description = "Company info, channel URLs and logo for a workspace")
public class WorkspaceConfigController {

    private final WorkspaceConfigCommandService commandService;
    private final WorkspaceConfigQueryService queryService;
    private final OwnershipGuard ownershipGuard;
    private final LogoStorageService logoStorageService;

    public WorkspaceConfigController(WorkspaceConfigCommandService commandService,
                                     WorkspaceConfigQueryService queryService,
                                     OwnershipGuard ownershipGuard,
                                     LogoStorageService logoStorageService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
        this.logoStorageService = logoStorageService;
    }

    @Operation(summary = "Get workspace config (company info, channel URLs, logo)")
    @GetMapping
    public ResponseEntity<WorkspaceConfigResource> getConfig(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        return queryService.handle(new GetWorkspaceConfigByWorkspaceIdQuery(workspaceId))
                .map(WorkspaceConfigAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create or update workspace config (upsert) — used both by the " +
            "onboarding flow (primer guardado) y la pantalla de Configuración (ediciones)")
    @PatchMapping
    public ResponseEntity<WorkspaceConfigResource> updateConfig(
            @PathVariable Long workspaceId,
            @Valid @RequestBody UpdateWorkspaceConfigResource resource) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var command = WorkspaceConfigAssembler.toCommand(workspaceId, resource);
        var updated = commandService.handle(command);
        return ResponseEntity.ok(WorkspaceConfigAssembler.toResource(updated));
    }

    @Operation(summary = "Upload/replace the workspace logo (png/jpeg/webp/svg, máx. 5MB)")
    @PostMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WorkspaceConfigResource> uploadLogo(
            @PathVariable Long workspaceId,
            @RequestParam("file") MultipartFile file) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var logoUrl = logoStorageService.store(file, workspaceId);

        var command = new UpdateWorkspaceConfigCommand(
                workspaceId, null, null, null, null, null, null, null, null, null, null, logoUrl);
        var updated = commandService.handle(command);
        return ResponseEntity.ok(WorkspaceConfigAssembler.toResource(updated));
    }
}