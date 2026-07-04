package brandradar.reputationmonitoring.interfaces.rest;

import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.reputationmonitoring.application.services.ReportFileGenerator;
import brandradar.reputationmonitoring.application.services.ReportGenerationService;
import brandradar.reputationmonitoring.domain.model.aggregates.ReputationReport;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationReportRepository;
import brandradar.reputationmonitoring.infrastructure.storage.ReportStorageService;
import brandradar.reputationmonitoring.interfaces.rest.resources.GenerateReportResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.ReputationReportResource;
import brandradar.shared.infrastructure.security.CurrentUser;
import brandradar.shared.infrastructure.security.OwnershipGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Generación, listado y descarga de reportes de reputación")
public class ReportsController {

    private final ReputationReportRepository reportRepository;
    private final BrandRepository brandRepository;
    private final ReportGenerationService reportGenerationService;
    private final ReportFileGenerator reportFileGenerator;
    private final ReportStorageService reportStorageService;
    private final OwnershipGuard ownershipGuard;
    private final CurrentUser currentUser;

    public ReportsController(ReputationReportRepository reportRepository,
                             BrandRepository brandRepository,
                             ReportGenerationService reportGenerationService,
                             ReportFileGenerator reportFileGenerator,
                             ReportStorageService reportStorageService,
                             OwnershipGuard ownershipGuard,
                             CurrentUser currentUser) {
        this.reportRepository = reportRepository;
        this.brandRepository = brandRepository;
        this.reportGenerationService = reportGenerationService;
        this.reportFileGenerator = reportFileGenerator;
        this.reportStorageService = reportStorageService;
        this.ownershipGuard = ownershipGuard;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Generate a new reputation report for a workspace's brand, for a given " +
            "date range and format (PDF/CSV/EXCEL). Incluye resumen ejecutivo generado con IA, " +
            "métricas con variación vs. periodo anterior, evolución diaria, top keywords y cuentas críticas.")
    @PostMapping("/api/v1/workspaces/{workspaceId}/reports")
    public ResponseEntity<ReputationReportResource> generateReport(
            @PathVariable Long workspaceId,
            @Valid @RequestBody GenerateReportResource resource) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);

        var brands = brandRepository.findByWorkspaceId(workspaceId);
        if (brands.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No brand found for this workspace");
        }
        Brand brand = brands.get(0);

        String title = "Reporte " + brand.getName() + " (" + resource.periodFrom() + " a " + resource.periodTo() + ")";
        var report = ReputationReport.create(workspaceId, brand.getId(), title,
                resource.periodFrom().atStartOfDay(java.time.ZoneOffset.UTC).toInstant(),
                resource.periodTo().atStartOfDay(java.time.ZoneOffset.UTC).toInstant(),
                resource.format().toUpperCase(), currentUser.get().userId());
        var saved = reportRepository.save(report);

        try {
            var data = reportGenerationService.generate(brand.getId(), brand.getName(),
                    resource.periodFrom(), resource.periodTo());

            byte[] fileContent = switch (resource.format().toLowerCase()) {
                case "excel", "xlsx" -> reportFileGenerator.toExcel(data);
                case "pdf" -> reportFileGenerator.toPdf(data);
                default -> reportFileGenerator.toCsv(data);
            };

            var stored = reportStorageService.store(fileContent, workspaceId, resource.format());
            var ready = saved.markReady(stored.url(), stored.sizeBytes());
            var finalReport = reportRepository.save(ready);

            return ResponseEntity.status(HttpStatus.CREATED).body(toResource(finalReport));
        } catch (Exception e) {
            log.error("Error generating report {}: {}", saved.getId(), e.getMessage());
            reportRepository.save(saved.markFailed());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not generate report");
        }
    }

    @Operation(summary = "List reports for a workspace, most recent first")
    @GetMapping("/api/v1/workspaces/{workspaceId}/reports")
    public ResponseEntity<List<ReputationReportResource>> listReports(@PathVariable Long workspaceId) {
        ownershipGuard.assertWorkspaceOwnership(workspaceId);
        var reports = reportRepository.findByWorkspaceIdOrderByCreatedAtDesc(workspaceId).stream()
                .map(this::toResource)
                .toList();
        return ResponseEntity.ok(reports);
    }

    @Operation(summary = "Download a generated report file")
    @GetMapping("/api/v1/reports/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        var report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));
        ownershipGuard.assertWorkspaceOwnership(report.getWorkspaceId());

        if (!"READY".equals(report.getStatus()) || report.getFileUrl() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Report is not ready yet");
        }

        try {
            var relativePath = report.getFileUrl().replaceFirst("^/uploads/", "");
            var filePath = Paths.get("uploads", relativePath);
            byte[] content = Files.readAllBytes(filePath);

            MediaType mediaType = switch (report.getFormat()) {
                case "EXCEL" -> MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                case "PDF" -> MediaType.APPLICATION_PDF;
                default -> MediaType.parseMediaType("text/csv");
            };

            String filename = filePath.getFileName().toString();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(mediaType)
                    .body(content);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not read report file");
        }
    }

    @Operation(summary = "Delete a report")
    @DeleteMapping("/api/v1/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        var report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));
        ownershipGuard.assertWorkspaceOwnership(report.getWorkspaceId());
        reportRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ReputationReportResource toResource(ReputationReport r) {
        return new ReputationReportResource(r.getId(), r.getWorkspaceId(), r.getBrandId(), r.getTitle(),
                r.getPeriodFrom(), r.getPeriodTo(), r.getStatus(), r.getFormat(), r.getFileUrl(),
                r.getFileSizeBytes(), r.getGeneratedAt(), r.getCreatedAt());
    }
}