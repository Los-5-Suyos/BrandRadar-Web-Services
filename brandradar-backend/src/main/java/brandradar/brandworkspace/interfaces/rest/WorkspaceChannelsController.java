package brandradar.brandworkspace.interfaces.rest;

import brandradar.brandworkspace.application.commands.AddWorkspaceChannelCommand;
import brandradar.brandworkspace.application.commandservices.WorkspaceChannelCommandService;
import brandradar.brandworkspace.application.queries.GetChannelsByWorkspaceIdQuery;
import brandradar.brandworkspace.application.queryservices.WorkspaceChannelQueryService;
import brandradar.brandworkspace.interfaces.rest.resources.AddWorkspaceChannelResource;
import brandradar.brandworkspace.interfaces.rest.resources.WorkspaceChannelResource;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import brandradar.brandworkspace.domain.model.services.ChannelPlanPolicy;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/workspaces/{workspaceId}/channels", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Workspace Channels", description = "CRUD endpoints for workspace channels")
public class WorkspaceChannelsController {

    private final WorkspaceChannelCommandService commandService;
    private final WorkspaceChannelQueryService queryService;
    private final OwnershipGuard ownershipGuard;
    private final BrandWorkspaceRepository brandWorkspaceRepository;

    public WorkspaceChannelsController(WorkspaceChannelCommandService commandService,
                                       WorkspaceChannelQueryService queryService,
                                       OwnershipGuard ownershipGuard,
                                       BrandWorkspaceRepository brandWorkspaceRepository) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
        this.brandWorkspaceRepository = brandWorkspaceRepository;
    }

    @Operation(summary = "List channels for a workspace")
    @GetMapping
    public ResponseEntity<List<WorkspaceChannelResource>> getChannels(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var channels = queryService.handle(new GetChannelsByWorkspaceIdQuery(workspaceId));
        var resources = channels.stream()
                .map(c -> new WorkspaceChannelResource(c.getId(), c.getWorkspaceId(), c.getChannelType()))
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Add a channel to a workspace")
    @PostMapping
    public ResponseEntity<WorkspaceChannelResource> addChannel(
            @PathVariable Long workspaceId,
            @Valid @RequestBody AddWorkspaceChannelResource resource) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var command = new AddWorkspaceChannelCommand(workspaceId, resource.channelType());
        var result = commandService.handle(command);
        return result
                .map(c -> new WorkspaceChannelResource(c.getId(), c.getWorkspaceId(), c.getChannelType()))
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @Operation(summary = "List all 8 channel types with an 'allowed' flag based on the " +
            "workspace's current plan — el frontend usa esto en vez de adivinar la matriz de planes")
    @GetMapping("/available")
    public ResponseEntity<List<java.util.Map<String, Object>>> getAvailableChannels(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var workspace = brandWorkspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Workspace not found"));

        var allowed = ChannelPlanPolicy.allowedFor(workspace.getPlan());

        var result = ChannelPlanPolicy.ALL_CHANNELS.stream()
                .map(channelType -> java.util.Map.<String, Object>of(
                        "channelType", channelType,
                        "allowed", allowed.contains(channelType)
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Remove a channel from a workspace")
    @DeleteMapping("/{channelType}")
    public ResponseEntity<Void> deleteChannel(
            @PathVariable Long workspaceId,
            @PathVariable String channelType) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        commandService.deleteByWorkspaceIdAndChannelType(workspaceId, channelType);
        return ResponseEntity.noContent().build();
    }
}