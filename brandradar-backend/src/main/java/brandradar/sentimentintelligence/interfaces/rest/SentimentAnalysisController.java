package brandradar.sentimentintelligence.interfaces.rest;

import brandradar.sentimentintelligence.application.commandservices.SentimentAnalysisCommandService;
import brandradar.sentimentintelligence.application.queries.GetSentimentAnalysisByBrandIdQuery;
import brandradar.sentimentintelligence.application.queryservices.SentimentAnalysisQueryService;
import brandradar.sentimentintelligence.interfaces.rest.resources.CreateSentimentAnalysisResource;
import brandradar.sentimentintelligence.interfaces.rest.resources.SentimentAnalysisResource;
import brandradar.sentimentintelligence.interfaces.rest.transform.SentimentAnalysisAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/sentiment-analysis", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Sentiment Analysis", description = "Sentiment Analysis management endpoints")
public class SentimentAnalysisController {

    private final SentimentAnalysisCommandService commandService;
    private final SentimentAnalysisQueryService queryService;

    public SentimentAnalysisController(SentimentAnalysisCommandService commandService,
                                       SentimentAnalysisQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a sentiment analysis")
    @PostMapping
    public ResponseEntity<SentimentAnalysisResource> createSentimentAnalysis(
            @Valid @RequestBody CreateSentimentAnalysisResource resource) {
        var command = SentimentAnalysisAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(SentimentAnalysisAssembler::toResource)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get sentiment analysis by brand ID")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<SentimentAnalysisResource>> getSentimentAnalysisByBrandId(
            @PathVariable Long brandId) {
        var analyses = queryService.handle(new GetSentimentAnalysisByBrandIdQuery(brandId));
        var resources = analyses.stream().map(SentimentAnalysisAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get sentiment analysis by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SentimentAnalysisResource> getSentimentAnalysisById(@PathVariable Long id) {
        return queryService.findById(id)
                .map(SentimentAnalysisAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}