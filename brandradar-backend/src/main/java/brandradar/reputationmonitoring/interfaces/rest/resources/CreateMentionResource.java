package brandradar.reputationmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateMentionResource(
        @NotNull Long brandId,
        @NotBlank String content,
        String sourcePlatform,
        String sourceUrl,
        String author,
        Instant publishedAt
) {}