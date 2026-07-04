package brandradar.sentimentintelligence.infrastructure.persistence.jpa.repositories;

import brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities.DashboardSnapshotJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SpringDataDashboardSnapshotRepository extends JpaRepository<DashboardSnapshotJpaEntity, Long> {
    Optional<DashboardSnapshotJpaEntity> findByBrandIdAndDate(Long brandId, LocalDate date);
    List<DashboardSnapshotJpaEntity> findTop30ByBrandIdOrderByDateDesc(Long brandId);
}