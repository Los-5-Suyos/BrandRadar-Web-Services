package brandradar.brandworkspace.interfaces.rest;

import brandradar.brandworkspace.application.commands.CreateKeywordRuleCommand;
import brandradar.brandworkspace.application.commandservices.KeywordRuleCommandService;
import brandradar.brandworkspace.application.queries.GetKeywordRulesByBrandIdQuery;
import brandradar.brandworkspace.application.queryservices.KeywordRuleQueryService;
import brandradar.brandworkspace.domain.model.aggregates.KeywordRule;
import brandradar.brandworkspace.interfaces.rest.resources.CreateKeywordRuleResource;
import brandradar.brandworkspace.interfaces.rest.resources.KeywordRuleResource;
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
@RequestMapping(value = "/api/v1/brands/{brandId}/keywords", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Keyword Rules", description = "Keywords de inclusión por marca (usadas para filtrar la ingesta de SociaVault)")
public class KeywordRulesController {

    private final KeywordRuleCommandService commandService;
    private final KeywordRuleQueryService queryService;
    private final OwnershipGuard ownershipGuard;

    public KeywordRulesController(KeywordRuleCommandService commandService,
                                  KeywordRuleQueryService queryService,
                                  OwnershipGuard ownershipGuard) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ownershipGuard = ownershipGuard;
    }

    @Operation(summary = "List inclusion keywords for a brand")
    @GetMapping
    public ResponseEntity<List<KeywordRuleResource>> getKeywords(@PathVariable Long brandId) {
        ownershipGuard.assertBrandOwnership(brandId);
        var rules = queryService.handle(new GetKeywordRulesByBrandIdQuery(brandId));
        var resources = rules.stream().map(this::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Add an inclusion keyword to a brand")
    @PostMapping
    public ResponseEntity<KeywordRuleResource> addKeyword(
            @PathVariable Long brandId,
            @Valid @RequestBody CreateKeywordRuleResource resource) {
        ownershipGuard.assertBrandOwnership(brandId);
        var command = new CreateKeywordRuleCommand(brandId, resource.keyword(), resource.matchType());
        var saved = commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResource(saved));
    }

    @Operation(summary = "Remove an inclusion keyword")
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable Long brandId, @PathVariable Long keywordId) {
        ownershipGuard.assertBrandOwnership(brandId);
        var rule = queryService.findById(keywordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Keyword not found"));
        if (!rule.getBrandId().equals(brandId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Keyword does not belong to this brand");
        }
        commandService.deleteById(keywordId);
        return ResponseEntity.noContent().build();
    }

    private KeywordRuleResource toResource(KeywordRule rule) {
        return new KeywordRuleResource(rule.getId(), rule.getBrandId(), rule.getKeyword(),
                rule.getMatchType(), rule.getWeight(), rule.getIsActive());
    }
}