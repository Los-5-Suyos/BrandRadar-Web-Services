package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationReportSchedule;

import java.util.Optional;

public interface ReputationReportScheduleRepository {
    ReputationReportSchedule save(ReputationReportSchedule schedule);
    Optional<ReputationReportSchedule> findByWorkspaceId(Long workspaceId);
    void deleteByWorkspaceId(Long workspaceId);
}