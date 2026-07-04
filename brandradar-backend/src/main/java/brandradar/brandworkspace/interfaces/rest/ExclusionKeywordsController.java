package brandradar.brandworkspace.interfaces.rest;

import brandradar.brandworkspace.application.commands.CreateExclusionKeywordCommand;
import brandradar.brandworkspace.application.commandservices.ExclusionKeywordCommandService;
import brandradar.brandworkspace.application.queries.GetExclusionKeywordsByWorkspaceIdQuery;
import brandradar.brandworkspace.application.queryservices.ExclusionKeywordQueryService;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceExclusionKeyword;
import brandradar.brandworkspace.interfaces.rest.resources.CreateExclusionKeywordResource;
import brandradar.brandworkspace.interfaces.rest.resources.ExclusionKeywordResource;
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
@RequestMapping(value = "/api/v1/workspaces/{workspaceId}/exclusion-keywords", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Exclusion Keywords", description = "Keywords de exclusión por workspace (filtran falsos positivos en la ingesta)")
public class ExclusionKeywordsController {

    private final ExclusionKeywordCommandService commandService;
    private final ExclusionKeywordQueryService queryService;
    private final OwnershipGuard ownershipGuard;

    public ExclusionKeywordsController(ExclusionKeywordCommandService commandService,
                                       ExclusionKeywordQueryService queryService,
                                       OwnershipGuard ownershipGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
    }

    @Operation(summary = "List exclusion keywords for a workspace")
    @GetMapping
    public ResponseEntity<List<ExclusionKeywordResource>> getExclusionKeywords(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var keywords = queryService.handle(new GetExclusionKeywordsByWorkspaceIdQuery(workspaceId));
        var resources = keywords.stream().map(this::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Add an exclusion keyword to a workspace")
    @PostMapping
    public ResponseEntity<ExclusionKeywordResource> addExclusionKeyword(
            @PathVariable Long workspaceId,
            @Valid @RequestBody CreateExclusionKeywordResource resource) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var command = new CreateExclusionKeywordCommand(workspaceId, resource.keyword());
        var saved = commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResource(saved));
    }

    @Operation(summary = "Remove an exclusion keyword")
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> deleteExclusionKeyword(
            @PathVariable Long workspaceId, @PathVariable Long keywordId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var keyword = queryService.findById(keywordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Keyword not found"));
        if (!keyword.getWorkspaceId().equals(workspaceId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Keyword does not belong to this workspace");
        }
        commandService.deleteById(keywordId);
        return ResponseEntity.noContent().build();
    }

    private ExclusionKeywordResource toResource(WorkspaceExclusionKeyword keyword) {
        return new ExclusionKeywordResource(keyword.getId(), keyword.getWorkspaceId(), keyword.getKeyword());
    }
}