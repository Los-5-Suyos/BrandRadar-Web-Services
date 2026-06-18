package brandradar.reputationmonitoring.infrastructure.persistence.jpa;

import brandradar.reputationmonitoring.domain.model.aggregates.DailyMetricSnapshot;
import brandradar.reputationmonitoring.domain.model.repositories.DailyMetricSnapshotRepository;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers.DailyMetricSnapshotPersistenceMapper;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories.SpringDataDailyMetricSnapshotRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DailyMetricSnapshotPersistenceAdapter implements DailyMetricSnapshotRepository {

    private final SpringDataDailyMetricSnapshotRepository springDataRepository;

    public DailyMetricSnapshotPersistenceAdapter(SpringDataDailyMetricSnapshotRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public DailyMetricSnapshot save(DailyMetricSnapshot snapshot) {
        var jpaEntity = DailyMetricSnapshotPersistenceMapper.toJpaEntity(snapshot);
        var saved = springDataRepository.save(jpaEntity);
        return DailyMetricSnapshotPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<DailyMetricSnapshot> findByWorkspaceIdAndSnapshotDate(Long workspaceId, LocalDate snapshotDate) {
        return springDataRepository.findByWorkspaceIdAndSnapshotDate(workspaceId, snapshotDate)
                .map(DailyMetricSnapshotPersistenceMapper::toDomain);
    }

    @Override
    public List<DailyMetricSnapshot> findByWorkspaceIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(
            Long workspaceId, LocalDate from, LocalDate to) {
        return springDataRepository.findByWorkspaceIdAndSnapshotDateBetweenOrderBySnapshotDateAsc(workspaceId, from, to)
                .stream()
                .map(DailyMetricSnapshotPersistenceMapper::toDomain)
                .toList();
    }
}
