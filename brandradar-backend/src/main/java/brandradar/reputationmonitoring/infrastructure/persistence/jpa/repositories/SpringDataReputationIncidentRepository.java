package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.domain.model.valueobjects.IncidentStatus;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationIncidentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataReputationIncidentRepository extends JpaRepository<ReputationIncidentJpaEntity, Long> {
    List<ReputationIncidentJpaEntity> findByBrandId(Long brandId);
    List<ReputationIncidentJpaEntity> findByStatus(String status);

    @Query("SELECT r FROM ReputationIncidentJpaEntity r WHERE r.brandId = :workspaceId AND r.status = :status ORDER BY r.createdAt DESC")
    List<ReputationIncidentJpaEntity> findByWorkspaceIdAndStatusOrderByDetectedAtDesc(
            @Param("workspaceId") Long workspaceId,
            @Param("status") String status);

    // Mapea workspaceId a brandId, status a String, y simula el filtro por title usando severityLabel
    @Query("SELECT r FROM ReputationIncidentJpaEntity r WHERE r.brandId = :workspaceId AND r.status = :status AND (:title IS NULL OR r.severityLabel LIKE %:title%)")
    Optional<ReputationIncidentJpaEntity> findByWorkspaceIdAndTitleAndStatus(
            @Param("workspaceId") Long workspaceId,
            @Param("title") String title,
            @Param("status") String status);
}