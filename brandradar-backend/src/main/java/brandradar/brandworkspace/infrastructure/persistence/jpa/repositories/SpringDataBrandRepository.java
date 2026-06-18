package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.BrandJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataBrandRepository extends JpaRepository<BrandJpaEntity, Long> {
    List<BrandJpaEntity> findByWorkspaceId(Long workspaceId);
}