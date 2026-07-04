package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.time.Instant;

public record ReputationReportResource(
        Long id,
        Long workspaceId,
        Long brandId,
        String title,
        Instant periodFrom,
        Instant periodTo,
        String status,
        String format,
        String fileUrl,
        Long fileSizeBytes,
        Instant generatedAt,
        Instant createdAt
) {}