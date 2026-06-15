package brandradar.infrastructurehealth.application.internal.queryservices;

import brandradar.infrastructurehealth.application.queries.GetAllHealthChecksQuery;
import brandradar.infrastructurehealth.application.queryservices.ServiceHealthCheckQueryService;
import brandradar.infrastructurehealth.domain.model.aggregates.ServiceHealthCheck;
import brandradar.infrastructurehealth.domain.model.repositories.ServiceHealthCheckRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceHealthCheckQueryServiceImpl implements ServiceHealthCheckQueryService {

    private final ServiceHealthCheckRepository serviceHealthCheckRepository;

    public ServiceHealthCheckQueryServiceImpl(ServiceHealthCheckRepository serviceHealthCheckRepository) {
        this.serviceHealthCheckRepository = serviceHealthCheckRepository;
    }

    @Override
    public List<ServiceHealthCheck> handle(GetAllHealthChecksQuery query) {
        return serviceHealthCheckRepository.findAll();
    }

    @Override
    public Optional<ServiceHealthCheck> findById(Long id) {
        return serviceHealthCheckRepository.findById(id);
    }
}