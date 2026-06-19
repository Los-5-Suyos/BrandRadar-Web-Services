package brandradar.reputationmonitoring.infrastructure.persistence.jpa.repositories;

import brandradar.reputationmonitoring.domain.model.repositories.IncidentRepository;
import brandradar.reputationmonitoring.domain.model.valueobjects.IncidentStatus;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationIncidentJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaIncidentRepositoryAdapter implements IncidentRepository {

    private final SpringDataReputationIncidentRepository springDataRepository;

    public JpaIncidentRepositoryAdapter(SpringDataReputationIncidentRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public List<ReputationIncidentJpaEntity> findByWorkspaceIdAndStatusOrderByDetectedAtDesc(Long workspaceId, IncidentStatus status) {
        return springDataRepository.findByWorkspaceIdAndStatusOrderByDetectedAtDesc(workspaceId, status.name());
    }

    @Override
    public Optional<ReputationIncidentJpaEntity> findByWorkspaceIdAndTitleAndStatus(Long workspaceId, String title, IncidentStatus status) {
        return springDataRepository.findByWorkspaceIdAndTitleAndStatus(workspaceId, title, status.name());
    }

    @Override
    public ReputationIncidentJpaEntity save(ReputationIncidentJpaEntity incident) {
        return springDataRepository.save(incident);
    }
}