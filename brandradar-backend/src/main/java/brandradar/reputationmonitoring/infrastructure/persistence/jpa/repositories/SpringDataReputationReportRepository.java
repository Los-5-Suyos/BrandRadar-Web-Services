package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationReportJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataReputationReportRepository extends JpaRepository<ReputationReportJpaEntity, Long> {
    List<ReputationReportJpaEntity> findByWorkspaceIdOrderByCreatedAtDesc(Long workspaceId);
}