package brandradar.brandworkspace.interfaces.rest;

import brandradar.brandworkspace.application.commandservices.BrandCommandService;
import brandradar.brandworkspace.application.queries.GetBrandsByWorkspaceIdQuery;
import brandradar.brandworkspace.application.queryservices.BrandQueryService;
import brandradar.brandworkspace.interfaces.rest.resources.BrandResource;
import brandradar.brandworkspace.interfaces.rest.resources.CreateBrandResource;
import brandradar.brandworkspace.interfaces.rest.transform.BrandResourceFromEntityAssembler;
import brandradar.brandworkspace.interfaces.rest.transform.CreateBrandCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/brands", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Brands", description = "Brand management endpoints")
public class BrandsController {

    private final BrandCommandService commandService;
    private final BrandQueryService queryService;

    public BrandsController(BrandCommandService commandService, BrandQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Operation(summary = "Create a new brand")
    @PostMapping
    public ResponseEntity<BrandResource> createBrand(@Valid @RequestBody CreateBrandResource resource) {
        var command = CreateBrandCommandFromResourceAssembler.toCommand(resource);
        var result = commandService.handle(command);
        return result
                .map(BrandResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get brands by workspace ID")
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<BrandResource>> getBrandsByWorkspaceId(@PathVariable Long workspaceId) {
        var brands = queryService.handle(new GetBrandsByWorkspaceIdQuery(workspaceId));
        var resources = brands.stream()
                .map(BrandResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get brand by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BrandResource> getBrandById(@PathVariable Long id) {
        return ResponseEntity.notFound().build();
    }
}