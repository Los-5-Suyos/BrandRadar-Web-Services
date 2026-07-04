package brandradar.reputationmonitoring.infrastructure.persistence.jpa;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationIncidentRepository;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers.ReputationIncidentPersistenceMapper;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories.SpringDataReputationIncidentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReputationIncidentPersistenceAdapter implements ReputationIncidentRepository {

    private final SpringDataReputationIncidentRepository springDataRepository;

    public ReputationIncidentPersistenceAdapter(SpringDataReputationIncidentRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ReputationIncident save(ReputationIncident incident) {
        var jpaEntity = ReputationIncidentPersistenceMapper.toJpaEntity(incident);
        var saved = springDataRepository.save(jpaEntity);
        return ReputationIncidentPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<ReputationIncident> findById(Long id) {
        return springDataRepository.findById(id)
                .map(ReputationIncidentPersistenceMapper::toDomain);
    }

    @Override
    public List<ReputationIncident> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(ReputationIncidentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<ReputationIncident> findByStatus(String status) {
        return springDataRepository.findByStatus(status)
                .stream()
                .map(ReputationIncidentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
}