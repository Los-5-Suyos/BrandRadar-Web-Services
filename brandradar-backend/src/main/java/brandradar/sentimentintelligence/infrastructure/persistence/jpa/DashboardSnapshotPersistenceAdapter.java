package brandradar.sentimentintelligence.infrastructure.persistence.jpa;

import brandradar.sentimentintelligence.domain.model.aggregates.DashboardSnapshot;
import brandradar.sentimentintelligence.domain.model.repositories.DashboardSnapshotRepository;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.mappers.DashboardSnapshotPersistenceMapper;
import brandradar.sentimentintelligence.infrastructure.persistence.jpa.repositories.SpringDataDashboardSnapshotRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DashboardSnapshotPersistenceAdapter implements DashboardSnapshotRepository {

    private final SpringDataDashboardSnapshotRepository springDataRepository;

    public DashboardSnapshotPersistenceAdapter(SpringDataDashboardSnapshotRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public DashboardSnapshot save(DashboardSnapshot snapshot) {
        var jpaEntity = DashboardSnapshotPersistenceMapper.toJpaEntity(snapshot);
        var saved = springDataRepository.save(jpaEntity);
        return DashboardSnapshotPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<DashboardSnapshot> findByBrandIdAndDate(Long brandId, LocalDate date) {
        return springDataRepository.findByBrandIdAndDate(brandId, date)
                .map(DashboardSnapshotPersistenceMapper::toDomain);
    }

    @Override
    public List<DashboardSnapshot> findLastNDaysByBrandId(Long brandId, int days) {
        return springDataRepository.findTop30ByBrandIdOrderByDateDesc(brandId)
                .stream()
                .limit(days)
                .map(DashboardSnapshotPersistenceMapper::toDomain)
                .toList();
    }
}