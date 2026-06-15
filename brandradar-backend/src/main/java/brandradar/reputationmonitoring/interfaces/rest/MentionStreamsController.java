package brandradar.reputationmonitoring.interfaces.rest;

import brandradar.reputationmonitoring.application.commandservices.MentionStreamCommandService;
import brandradar.reputationmonitoring.application.queries.GetMentionStreamsByBrandIdQuery;
import brandradar.reputationmonitoring.application.queryservices.MentionStreamQueryService;
import brandradar.reputationmonitoring.interfaces.rest.resources.CreateMentionStreamResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.MentionStreamResource;
import brandradar.reputationmonitoring.interfaces.rest.transform.MentionStreamAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/mention-streams", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Mention Streams", description = "Mention Stream management endpoints")
public class MentionStreamsController {

    private final MentionStreamCommandService commandService;
    private final MentionStreamQueryService queryService;

    public MentionStreamsController(MentionStreamCommandService commandService,
                                    MentionStreamQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a mention stream")
    @PostMapping
    public ResponseEntity<MentionStreamResource> createMentionStream(
            @Valid @RequestBody CreateMentionStreamResource resource) {
        var command = MentionStreamAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(MentionStreamAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get mention streams by brand ID")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<MentionStreamResource>> getMentionStreamsByBrandId(
            @PathVariable Long brandId) {
        var streams = queryService.handle(new GetMentionStreamsByBrandIdQuery(brandId));
        var resources = streams.stream().map(MentionStreamAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }
}