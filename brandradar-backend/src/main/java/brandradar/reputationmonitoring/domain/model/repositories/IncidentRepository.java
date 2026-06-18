package brandradar.reputationmonitoring.domain.model.repositories;

import brandradar.reputationmonitoring.domain.model.valueobjects.IncidentStatus;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationIncidentJpaEntity;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository {
    List<ReputationIncidentJpaEntity> findByWorkspaceIdAndStatusOrderByDetectedAtDesc(Long workspaceId, IncidentStatus status);
    Optional<ReputationIncidentJpaEntity> findByWorkspaceIdAndTitleAndStatus(Long workspaceId, String title, IncidentStatus status);
    ReputationIncidentJpaEntity save(ReputationIncidentJpaEntity incident);
}