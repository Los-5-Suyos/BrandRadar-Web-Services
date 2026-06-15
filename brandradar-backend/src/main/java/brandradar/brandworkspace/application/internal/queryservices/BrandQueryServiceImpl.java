package brandradar.brandworkspace.application.internal.queryservices;

import brandradar.brandworkspace.application.queries.GetBrandsByWorkspaceIdQuery;
import brandradar.brandworkspace.application.queryservices.BrandQueryService;
import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandQueryServiceImpl implements BrandQueryService {

    private final BrandRepository brandRepository;

    public BrandQueryServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> handle(GetBrandsByWorkspaceIdQuery query) {
        return brandRepository.findByWorkspaceId(query.workspaceId());
    }
}