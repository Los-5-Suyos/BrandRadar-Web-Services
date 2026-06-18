package brandradar.infrastructurehealth.application.internal.queryservices;

import brandradar.infrastructurehealth.application.queries.GetInfraIncidentsByStatusQuery;
import brandradar.infrastructurehealth.application.queryservices.InfraIncidentQueryService;
import brandradar.infrastructurehealth.domain.model.aggregates.InfraIncident;
import brandradar.infrastructurehealth.domain.model.repositories.InfraIncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfraIncidentQueryServiceImpl implements InfraIncidentQueryService {

    private final InfraIncidentRepository infraIncidentRepository;

    public InfraIncidentQueryServiceImpl(InfraIncidentRepository infraIncidentRepository) {
        this.infraIncidentRepository = infraIncidentRepository;
    }

    @Override
    public List<InfraIncident> handle(GetInfraIncidentsByStatusQuery query) {
        return infraIncidentRepository.findByStatus(query.status());
    }

    @Override
    public Optional<InfraIncident> findById(Long id) {
        return infraIncidentRepository.findById(id);
    }
}