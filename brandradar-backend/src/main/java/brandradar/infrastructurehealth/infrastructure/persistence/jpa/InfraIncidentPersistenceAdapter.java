package brandradar.infrastructurehealth.infrastructure.persistence.jpa;

import brandradar.infrastructurehealth.domain.model.aggregates.InfraIncident;
import brandradar.infrastructurehealth.domain.model.repositories.InfraIncidentRepository;
import brandradar.infrastructurehealth.infrastructure.persistence.jpa.mappers.InfraIncidentPersistenceMapper;
import brandradar.infrastructurehealth.infrastructure.persistence.jpa.repositories.SpringDataInfraIncidentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InfraIncidentPersistenceAdapter implements InfraIncidentRepository {

    private final SpringDataInfraIncidentRepository springDataRepository;

    public InfraIncidentPersistenceAdapter(SpringDataInfraIncidentRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public InfraIncident save(InfraIncident incident) {
        var jpaEntity = InfraIncidentPersistenceMapper.toJpaEntity(incident);
        var saved = springDataRepository.save(jpaEntity);
        return InfraIncidentPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<InfraIncident> findById(Long id) {
        return springDataRepository.findById(id)
                .map(InfraIncidentPersistenceMapper::toDomain);
    }

    @Override
    public List<InfraIncident> findByServiceHealthCheckId(Long serviceHealthCheckId) {
        return springDataRepository.findByServiceHealthCheckId(serviceHealthCheckId)
                .stream()
                .map(InfraIncidentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<InfraIncident> findByStatus(String status) {
        return springDataRepository.findByStatus(status)
                .stream()
                .map(InfraIncidentPersistenceMapper::toDomain)
                .toList();
    }
}