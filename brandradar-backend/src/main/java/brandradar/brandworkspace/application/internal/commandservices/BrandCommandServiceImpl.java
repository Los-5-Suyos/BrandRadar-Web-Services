package brandradar.brandworkspace.application.internal.commandservices;

import brandradar.brandworkspace.application.commands.CreateBrandCommand;
import brandradar.brandworkspace.application.commandservices.BrandCommandService;
import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class BrandCommandServiceImpl implements BrandCommandService {

    private final BrandRepository brandRepository;

    public BrandCommandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional
    public Optional<Brand> handle(CreateBrandCommand command) {
        var brand = Brand.create(command.workspaceId(), command.name());
        var saved = brandRepository.save(brand);
        log.info("Brand created with id={}", saved.getId());
        return Optional.of(saved);
    }
}