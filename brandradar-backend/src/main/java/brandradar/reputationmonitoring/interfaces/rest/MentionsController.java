package brandradar.reputationmonitoring.interfaces.rest;

import brandradar.reputationmonitoring.application.commandservices.MentionCommandService;
import brandradar.reputationmonitoring.application.queries.GetMentionsByBrandIdQuery;
import brandradar.reputationmonitoring.application.queryservices.MentionQueryService;
import brandradar.reputationmonitoring.interfaces.rest.resources.CreateMentionResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.MentionResource;
import brandradar.reputationmonitoring.interfaces.rest.transform.MentionAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/mentions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Mentions", description = "Mention management endpoints")
public class MentionsController {

    private final MentionCommandService commandService;
    private final MentionQueryService queryService;

    public MentionsController(MentionCommandService commandService,
                              MentionQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a mention")
    @PostMapping
    public ResponseEntity<MentionResource> createMention(
            @Valid @RequestBody CreateMentionResource resource) {
        var command = MentionAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(MentionAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get mentions by brand ID")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<MentionResource>> getMentionsByBrandId(@PathVariable Long brandId) {
        var mentions = queryService.handle(new GetMentionsByBrandIdQuery(brandId));
        var resources = mentions.stream().map(MentionAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }
}