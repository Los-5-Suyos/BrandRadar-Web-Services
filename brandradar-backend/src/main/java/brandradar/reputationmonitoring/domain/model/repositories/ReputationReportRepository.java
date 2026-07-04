package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationReport;

import java.util.List;
import java.util.Optional;

public interface ReputationReportRepository {
    ReputationReport save(ReputationReport report);
    Optional<ReputationReport> findById(Long id);
    List<ReputationReport> findByWorkspaceIdOrderByCreatedAtDesc(Long workspaceId);
    void deleteById(Long id);
}