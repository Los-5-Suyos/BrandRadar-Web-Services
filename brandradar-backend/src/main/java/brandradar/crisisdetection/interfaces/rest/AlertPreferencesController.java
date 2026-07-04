package brandradar.crisisdetection.interfaces.rest;

import brandradar.crisisdetection.application.services.AlertPreferenceService;
import brandradar.crisisdetection.interfaces.rest.resources.AlertPreferenceResource;
import brandradar.crisisdetection.interfaces.rest.resources.UpdateAlertPreferenceResource;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/brands/{brandId}/alert-preferences", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Alert Preferences", description = "Toggles para qué tipos de alerta generar por marca")
public class AlertPreferencesController {

    private final AlertPreferenceService service;
    private final OwnershipGuard ownershipGuard;

    public AlertPreferencesController(AlertPreferenceService service, OwnershipGuard ownershipGuard) {
        this.service = service;
        this.ownershipGuard = ownershipGuard;
    }

    @Operation(summary = "List the 5 alert preferences for a brand (defaults to enabled=true if never configured)")
    @GetMapping
    public ResponseEntity<List<AlertPreferenceResource>> getPreferences(@PathVariable Long brandId) {
        ownershipGuard.assertBrandOwnership(brandId);
        var preferences = service.getAllForBrand(brandId).stream()
                .map(p -> new AlertPreferenceResource(p.getKey(), p.getEnabled()))
                .toList();
        return ResponseEntity.ok(preferences);
    }

    @Operation(summary = "Toggle one alert preference on/off")
    @PatchMapping("/{key}")
    public ResponseEntity<AlertPreferenceResource> updatePreference(
            @PathVariable Long brandId,
            @PathVariable String key,
            @Valid @RequestBody UpdateAlertPreferenceResource resource) {
        ownershipGuard.assertBrandOwnership(brandId);
        var updated = service.updatePreference(brandId, key, resource.enabled());
        return ResponseEntity.ok(new AlertPreferenceResource(updated.getKey(), updated.getEnabled()));
    }
}