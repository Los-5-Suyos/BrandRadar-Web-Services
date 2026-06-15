package brandradar.brandworkspace.infrastructure.persistence.jpa.mappers;

import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.BrandJpaEntity;

public class BrandPersistenceMapper {

    private BrandPersistenceMapper() {}

    public static BrandJpaEntity toJpaEntity(Brand brand) {
        return new BrandJpaEntity(
                brand.getId(),
                brand.getWorkspaceId(),
                brand.getName(),
                brand.getReputationScore(),
                brand.getReputationCalculatedAt()
        );
    }

    public static Brand toDomain(BrandJpaEntity entity) {
        return Brand.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                entity.getName(),
                entity.getReputationScore(),
                entity.getReputationCalculatedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}