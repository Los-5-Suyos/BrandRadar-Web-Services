package brandradar.shared.interfaces.rest.resources;

import java.util.List;

public record CriticalKeywordsResource(
        Long brandId,
        List<KeywordCount> keywords
) {
    public record KeywordCount(
            String keyword,
            Long count,
            Double percentOfMax
    ) {}
}