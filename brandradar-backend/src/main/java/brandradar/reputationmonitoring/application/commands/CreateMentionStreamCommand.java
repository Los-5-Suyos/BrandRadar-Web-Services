package brandradar.reputationmonitoring.application.commands;

import java.time.Instant;

public record CreateMentionStreamCommand(
        Long brandId,
        Instant periodFrom,
        Instant periodTo
) {}