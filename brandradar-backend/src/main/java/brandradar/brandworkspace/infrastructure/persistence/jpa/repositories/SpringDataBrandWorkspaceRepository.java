package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.BrandWorkspaceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataBrandWorkspaceRepository extends JpaRepository<BrandWorkspaceJpaEntity, Long> {
    List<BrandWorkspaceJpaEntity> findByUserId(Long userId);
}