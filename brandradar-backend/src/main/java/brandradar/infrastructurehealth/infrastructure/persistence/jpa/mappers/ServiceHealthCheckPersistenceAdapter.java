package brandradar.infrastructurehealth.infrastructure.persistence.jpa;

import brandradar.infrastructurehealth.domain.model.aggregates.ServiceHealthCheck;
import brandradar.infrastructurehealth.domain.model.repositories.ServiceHealthCheckRepository;
import brandradar.infrastructurehealth.infrastructure.persistence.jpa.mappers.ServiceHealthCheckPersistenceMapper;
import brandradar.infrastructurehealth.infrastructure.persistence.jpa.repositories.SpringDataServiceHealthCheckRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ServiceHealthCheckPersistenceAdapter implements ServiceHealthCheckRepository {

    private final SpringDataServiceHealthCheckRepository springDataRepository;

    public ServiceHealthCheckPersistenceAdapter(SpringDataServiceHealthCheckRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ServiceHealthCheck save(ServiceHealthCheck healthCheck) {
        var jpaEntity = ServiceHealthCheckPersistenceMapper.toJpaEntity(healthCheck);
        var saved = springDataRepository.save(jpaEntity);
        return ServiceHealthCheckPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ServiceHealthCheck> findById(Long id) {
        return springDataRepository.findById(id)
                .map(ServiceHealthCheckPersistenceMapper::toDomain);
    }

    @Override
    public List<ServiceHealthCheck> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(ServiceHealthCheckPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<ServiceHealthCheck> findByStatus(String status) {
        return springDataRepository.findByStatus(status)
                .stream()
                .map(ServiceHealthCheckPersistenceMapper::toDomain)
                .toList();
    }
}