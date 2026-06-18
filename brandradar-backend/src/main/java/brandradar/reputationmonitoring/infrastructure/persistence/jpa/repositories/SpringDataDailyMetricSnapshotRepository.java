package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.DailyMetricSnapshotJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SpringDataDailyMetricSnapshotRepository extends JpaRepository<DailyMetricSnapshotJpaEntity, Long> {

    Optional<DailyMetricSnapshotJpaEntity> findByWorkspaceIdAndSnapshotDate(Long workspaceId, LocalDate snapshotDate);

    List<DailyMetricSnapshotJpaEntity> findByWorkspaceIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(
            Long workspaceId, LocalDate from, LocalDate to);
}
