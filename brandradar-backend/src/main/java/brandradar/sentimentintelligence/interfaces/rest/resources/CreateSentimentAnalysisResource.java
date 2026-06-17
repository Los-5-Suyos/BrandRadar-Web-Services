package brandradar.sentimentintelligence.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateSentimentAnalysisResource(
        @NotNull Long brandId,
        @NotNull Instant periodFrom,
        @NotNull Instant periodTo
) {}