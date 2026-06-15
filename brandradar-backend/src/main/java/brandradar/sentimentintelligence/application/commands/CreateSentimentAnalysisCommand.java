package brandradar.sentimentintelligence.application.commands;

import java.time.Instant;

public record CreateSentimentAnalysisCommand(
        Long brandId,
        Instant periodFrom,
        Instant periodTo
) {}