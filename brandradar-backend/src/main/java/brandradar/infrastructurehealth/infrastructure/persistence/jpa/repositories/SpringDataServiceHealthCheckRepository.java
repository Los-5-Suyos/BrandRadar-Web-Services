package brandradar.infrastructurehealth.infrastructure.persistence.jpa.repositories;

import brandradar.infrastructurehealth.infrastructure.persistence.jpa.entities.ServiceHealthCheckJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataServiceHealthCheckRepository extends JpaRepository<ServiceHealthCheckJpaEntity, Long> {
    List<ServiceHealthCheckJpaEntity> findByStatus(String status);
}