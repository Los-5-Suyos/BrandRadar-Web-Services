package brandradar.crisisdetection.interfaces.rest;

import brandradar.crisisdetection.application.services.CrisisResponseEngineService;
import brandradar.crisisdetection.interfaces.rest.resources.CrisisAnalysisResource;
import brandradar.crisisdetection.interfaces.rest.resources.CrisisAnalysisResultResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/crisis-engine", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Crisis Response Engine", description = "AI-powered crisis analysis via Groq API")
public class CrisisResponseEngineController {

    private final CrisisResponseEngineService crisisResponseEngineService;

    public CrisisResponseEngineController(CrisisResponseEngineService crisisResponseEngineService) {
        this.crisisResponseEngineService = crisisResponseEngineService;
    }

    @Operation(summary = "Analyze a brand crisis using AI (Groq llama-3.3-70b-versatile)")
    @PostMapping("/analyze")
    public ResponseEntity<CrisisAnalysisResultResource> analyzeCrisis(
            @Valid @RequestBody CrisisAnalysisResource resource) {
        var result = crisisResponseEngineService.analyzeCrisis(
                resource.brandName(),
                resource.crisisDescription()
        );
        return ResponseEntity.ok(new CrisisAnalysisResultResource(
                result.pattern(),
                result.keywords(),
                result.geofocus(),
                result.diagnostico(),
                result.accion()
        ));
    }
}