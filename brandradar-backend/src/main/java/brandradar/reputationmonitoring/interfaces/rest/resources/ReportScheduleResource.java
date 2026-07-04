package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.time.Instant;

public record ReportScheduleResource(
        Long id,
        Long workspaceId,
        String email,
        String frequency,
        String dayOfWeek,
        String format,
        Boolean isActive,
        Instant nextRunAt
) {}