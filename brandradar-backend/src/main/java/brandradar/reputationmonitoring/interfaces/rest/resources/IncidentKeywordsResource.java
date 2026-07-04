package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.util.List;

public record IncidentKeywordsResource(
        Long incidentId,
        List<KeywordCount> keywords
) {
    public record KeywordCount(String keyword, Long count, Double percentOfMax) {}
}