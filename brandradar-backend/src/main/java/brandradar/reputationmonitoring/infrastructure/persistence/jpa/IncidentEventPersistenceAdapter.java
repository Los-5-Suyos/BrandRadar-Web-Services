package brandradar.reputationmonitoring.infrastructure.persistence.jpa;

import brandradar.reputationmonitoring.domain.model.aggregates.IncidentEvent;
import brandradar.reputationmonitoring.domain.model.repositories.IncidentEventRepository;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers.IncidentEventPersistenceMapper;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories.SpringDataIncidentEventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IncidentEventPersistenceAdapter implements IncidentEventRepository {

    private final SpringDataIncidentEventRepository springDataRepository;

    public IncidentEventPersistenceAdapter(SpringDataIncidentEventRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public IncidentEvent save(IncidentEvent event) {
        var jpaEntity = IncidentEventPersistenceMapper.toJpaEntity(event);
        var saved = springDataRepository.save(jpaEntity);
        return IncidentEventPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<IncidentEvent> findByIncidentId(Long incidentId) {
        return springDataRepository.findByIncidentId(incidentId)
                .stream()
                .map(IncidentEventPersistenceMapper::toDomain)
                .toList();
    }
}