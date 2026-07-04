package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationReportScheduleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataReputationReportScheduleRepository extends JpaRepository<ReputationReportScheduleJpaEntity, Long> {
    Optional<ReputationReportScheduleJpaEntity> findByWorkspaceId(Long workspaceId);
    void deleteByWorkspaceId(Long workspaceId);
}