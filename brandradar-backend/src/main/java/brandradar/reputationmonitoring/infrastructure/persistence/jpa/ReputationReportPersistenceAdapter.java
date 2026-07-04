package brandradar.reputationmonitoring.infrastructure.persistence.jpa;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationReport;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationReportRepository;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers.ReputationReportPersistenceMapper;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories.SpringDataReputationReportRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReputationReportPersistenceAdapter implements ReputationReportRepository {

    private final SpringDataReputationReportRepository springDataRepository;

    public ReputationReportPersistenceAdapter(SpringDataReputationReportRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ReputationReport save(ReputationReport report) {
        var jpaEntity = ReputationReportPersistenceMapper.toJpaEntity(report);
        var saved = springDataRepository.save(jpaEntity);
        return ReputationReportPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ReputationReport> findById(Long id) {
        return springDataRepository.findById(id)
                .map(ReputationReportPersistenceMapper::toDomain);
    }

    @Override
    public List<ReputationReport> findByWorkspaceIdOrderByCreatedAtDesc(Long workspaceId) {
        return springDataRepository.findByWorkspaceIdOrderByCreatedAtDesc(workspaceId)
                .stream()
                .map(ReputationReportPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
}