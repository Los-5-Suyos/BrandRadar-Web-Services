package brandradar.reputationmonitoring.application.internal.queryservices;

import brandradar.reputationmonitoring.application.queries.GetIncidentsByBrandIdQuery;
import brandradar.reputationmonitoring.application.queryservices.ReputationIncidentQueryService;
import brandradar.reputationmonitoring.domain.model.aggregates.ReputationIncident;
import brandradar.reputationmonitoring.domain.model.repositories.ReputationIncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReputationIncidentQueryServiceImpl implements ReputationIncidentQueryService {

    private final ReputationIncidentRepository reputationIncidentRepository;

    public ReputationIncidentQueryServiceImpl(ReputationIncidentRepository reputationIncidentRepository) {
        this.reputationIncidentRepository = reputationIncidentRepository;
    }

    @Override
    public List<ReputationIncident> handle(GetIncidentsByBrandIdQuery query) {
        return reputationIncidentRepository.findByBrandId(query.brandId());
    }

    @Override
    public Optional<ReputationIncident> findById(Long id) {
        return reputationIncidentRepository.findById(id);
    }
}