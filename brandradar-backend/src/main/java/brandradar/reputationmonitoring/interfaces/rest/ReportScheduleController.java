package brandradar.reputationmonitoring.interfaces.rest;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationReportSchedule;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationReportScheduleRepository;
import brandradar.reputationmonitoring.interfaces.rest.resources.ReportScheduleResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.UpsertReportScheduleResource;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/workspaces/{workspaceId}/report-schedule", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Report Schedule", description = "Programar el envío automático de reportes por email. " +
        "NOTA: guarda la configuración de verdad, pero el envío real requiere un servicio SMTP " +
        "que todavía no está conectado (mismo caso que forgot-password) — por ahora el 'envío' " +
        "quedaría simulado/logueado cuando se implemente el job de envío.")
public class ReportScheduleController {

    private final ReputationReportScheduleRepository scheduleRepository;
    private final OwnershipGuard ownershipGuard;

    public ReportScheduleController(ReputationReportScheduleRepository scheduleRepository,
                                    OwnershipGuard ownershipGuard) {
        this.scheduleRepository = scheduleRepository;
        this.ownershipGuard = ownershipGuard;
    }

    @Operation(summary = "Get the report schedule configuration for a workspace")
    @GetMapping
    public ResponseEntity<ReportScheduleResource> getSchedule(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        return scheduleRepository.findByWorkspaceId(workspaceId)
                .map(this::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create or update the report schedule (upsert) — one schedule per workspace")
    @PostMapping
    public ResponseEntity<ReportScheduleResource> upsertSchedule(
            @PathVariable Long workspaceId,
            @Valid @RequestBody UpsertReportScheduleResource resource) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        var existing = scheduleRepository.findByWorkspaceId(workspaceId);
        var toSave = existing.isPresent()
                ? existing.get().withUpdates(resource.email(), resource.frequency(),
                resource.dayOfWeek(), resource.format(), true)
                : ReputationReportSchedule.create(workspaceId, resource.email(),
                resource.frequency(), resource.dayOfWeek(), resource.format());

        var saved = scheduleRepository.save(toSave);
        return ResponseEntity.ok(toResource(saved));
    }

    @Operation(summary = "Delete/cancel the report schedule for a workspace")
    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        scheduleRepository.deleteByWorkspaceId(workspaceId);
        return ResponseEntity.noContent().build();
    }

    private ReportScheduleResource toResource(ReputationReportSchedule s) {
        return new ReportScheduleResource(s.getId(), s.getWorkspaceId(), s.getEmail(), s.getFrequency(),
                s.getDayOfWeek(), s.getFormat(), s.getIsActive(), s.getNextRunAt());
    }
}