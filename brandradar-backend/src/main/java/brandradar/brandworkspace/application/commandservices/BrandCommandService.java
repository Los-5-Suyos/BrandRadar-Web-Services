package brandradar.brandworkspace.application.commandservices;

import brandradar.brandworkspace.application.commands.CreateBrandCommand;
import brandradar.brandworkspace.domain.model.aggregates.Brand;

import java.util.Optional;

public interface BrandCommandService {
    Optional<Brand> handle(CreateBrandCommand command);
}