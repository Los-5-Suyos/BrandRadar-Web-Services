package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.aggregates.DailyMetricSnapshot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyMetricSnapshotRepository {
    DailyMetricSnapshot save(DailyMetricSnapshot snapshot);

    Optional<DailyMetricSnapshot> findByWorkspaceIdAndSnapshotDate(Long workspaceId, LocalDate snapshotDate);

    List<DailyMetricSnapshot> findByWorkspaceIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(
            Long workspaceId, LocalDate from, LocalDate to);
}
