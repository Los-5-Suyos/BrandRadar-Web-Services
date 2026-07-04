package brandradar.reputationmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GenerateReportResource(
        @NotNull LocalDate periodFrom,
        @NotNull LocalDate periodTo,
        @NotNull String format // PDF, CSV, EXCEL
) {}