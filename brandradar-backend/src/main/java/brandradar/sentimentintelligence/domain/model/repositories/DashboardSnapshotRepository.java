package brandradar.sentimentintelligence.domain.model.repositories;

import brandradar.sentimentintelligence.domain.model.aggregates.DashboardSnapshot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DashboardSnapshotRepository {
    DashboardSnapshot save(DashboardSnapshot snapshot);
    Optional<DashboardSnapshot> findByBrandIdAndDate(Long brandId, LocalDate date);
    List<DashboardSnapshot> findLastNDaysByBrandId(Long brandId, int days);
}