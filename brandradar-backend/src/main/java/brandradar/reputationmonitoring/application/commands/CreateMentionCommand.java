package brandradar.reputationmonitoring.application.commands;

import java.time.Instant;

public record CreateMentionCommand(
        Long brandId,
        String content,
        String sourcePlatform,
        String sourceUrl,
        String author,
        Instant publishedAt
) {}