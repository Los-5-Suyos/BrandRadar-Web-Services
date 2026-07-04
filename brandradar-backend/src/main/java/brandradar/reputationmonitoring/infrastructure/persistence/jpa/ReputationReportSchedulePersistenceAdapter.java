package brandradar.reputationmonitoring.infrastructure.persistence.jpa;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationReportSchedule;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationReportScheduleRepository;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers.ReputationReportSchedulePersistenceMapper;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories.SpringDataReputationReportScheduleRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReputationReportSchedulePersistenceAdapter implements ReputationReportScheduleRepository {

    private final SpringDataReputationReportScheduleRepository springDataRepository;

    public ReputationReportSchedulePersistenceAdapter(SpringDataReputationReportScheduleRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ReputationReportSchedule save(ReputationReportSchedule schedule) {
        var jpaEntity = ReputationReportSchedulePersistenceMapper.toJpaEntity(schedule);
        var saved = springDataRepository.save(jpaEntity);
        return ReputationReportSchedulePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ReputationReportSchedule> findByWorkspaceId(Long workspaceId) {
        return springDataRepository.findByWorkspaceId(workspaceId)
                .map(ReputationReportSchedulePersistenceMapper::toDomain);
    }

    @Override
    public void deleteByWorkspaceId(Long workspaceId) {
        springDataRepository.deleteByWorkspaceId(workspaceId);
    }
}