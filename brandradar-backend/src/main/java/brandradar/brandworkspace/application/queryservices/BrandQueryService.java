package brandradar.brandworkspace.application.queryservices;

import brandradar.brandworkspace.application.queries.GetBrandsByWorkspaceIdQuery;
import brandradar.brandworkspace.domain.model.aggregates.Brand;

import java.util.List;

public interface BrandQueryService {
    List<Brand> handle(GetBrandsByWorkspaceIdQuery query);
}