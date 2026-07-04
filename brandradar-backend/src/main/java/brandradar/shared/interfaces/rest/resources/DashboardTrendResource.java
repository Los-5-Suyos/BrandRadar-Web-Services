package brandradar.shared.interfaces.rest.resources;

import java.time.LocalDate;
import java.util.List;

public record DashboardTrendResource(
        Long brandId,
        List<TrendPoint> points
) {
    public record TrendPoint(
            LocalDate date,
            Double sentimentScore,
            Long mentionsCount
    ) {}
}