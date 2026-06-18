package brandradar.infrastructurehealth.infrastructure.persistence.jpa.repositories;

import brandradar.infrastructurehealth.infrastructure.persistence.jpa.entities.InfraIncidentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataInfraIncidentRepository extends JpaRepository<InfraIncidentJpaEntity, Long> {
    List<InfraIncidentJpaEntity> findByServiceHealthCheckId(Long serviceHealthCheckId);
    List<InfraIncidentJpaEntity> findByStatus(String status);
}