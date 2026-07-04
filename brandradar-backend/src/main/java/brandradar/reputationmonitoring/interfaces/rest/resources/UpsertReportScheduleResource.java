package brandradar.reputationmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpsertReportScheduleResource(
        @NotBlank @Email String email,
        @NotBlank String frequency, // WEEKLY, MONTHLY
        String dayOfWeek,           // MONDAY, TUESDAY, etc. (solo aplica si frequency=WEEKLY)
        @NotBlank String format     // PDF, CSV, EXCEL
) {}