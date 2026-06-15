package brandradar.brandworkspace.infrastructure.persistence.jpa;

import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.mappers.BrandPersistenceMapper;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataBrandRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BrandPersistenceAdapter implements BrandRepository {

    private final SpringDataBrandRepository springDataRepository;

    public BrandPersistenceAdapter(SpringDataBrandRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Brand save(Brand brand) {
        var jpaEntity = BrandPersistenceMapper.toJpaEntity(brand);
        var saved = springDataRepository.save(jpaEntity);
        return BrandPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return springDataRepository.findById(id)
                .map(BrandPersistenceMapper::toDomain);
    }

    @Override
    public List<Brand> findByWorkspaceId(Long workspaceId) {
        return springDataRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(BrandPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
}