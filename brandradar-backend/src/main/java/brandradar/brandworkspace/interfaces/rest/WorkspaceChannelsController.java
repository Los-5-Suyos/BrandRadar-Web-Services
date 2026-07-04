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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/workspaces/{workspaceId}/channels", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Workspace Channels", description = "CRUD endpoints for workspace channels")
public class WorkspaceChannelsController {

    private final WorkspaceChannelCommandService commandService;
    private final WorkspaceChannelQueryService queryService;
    private final OwnershipGuard ownershipGuard;

    public WorkspaceChannelsController(WorkspaceChannelCommandService commandService,
                                       WorkspaceChannelQueryService queryService,
                                       OwnershipGuard ownershipGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
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