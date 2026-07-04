package brandradar.crisisdetection.domain.model.repositories;

import brandradar.crisisdetection.domain.model.aggregates.CrisisAnalysisLog;

import java.util.List;

public interface CrisisAnalysisLogRepository {
    CrisisAnalysisLog save(CrisisAnalysisLog log);
    List<CrisisAnalysisLog> findByIncidentIdOrderByCreatedAtDesc(Long incidentId);
}