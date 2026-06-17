package brandradar.crisisdetection.application.internal.queryservices;

import brandradar.crisisdetection.application.queries.GetCrisisAlertsByBrandIdQuery;
import brandradar.crisisdetection.application.queryservices.CrisisAlertQueryService;
import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrisisAlertQueryServiceImpl implements CrisisAlertQueryService {

    private final CrisisAlertRepository crisisAlertRepository;

    public CrisisAlertQueryServiceImpl(CrisisAlertRepository crisisAlertRepository) {
        this.crisisAlertRepository = crisisAlertRepository;
    }

    @Override
    public List<CrisisAlert> handle(GetCrisisAlertsByBrandIdQuery query) {
        return crisisAlertRepository.findByBrandId(query.brandId());
    }

    @Override
    public Optional<CrisisAlert> findById(Long id) {
        return crisisAlertRepository.findById(id);
    }
}