package brandradar.brandworkspace.infrastructure.persistence.jpa.repositories;

import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.BrandWorkspaceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataBrandWorkspaceRepository extends JpaRepository<BrandWorkspaceJpaEntity, Long> {
    List<BrandWorkspaceJpaEntity> findByUserId(Long userId);
    Optional<BrandWorkspaceJpaEntity> findByIdAndUserId(Long id, Long userId);
    List<BrandWorkspaceJpaEntity> findByStatus(String status);
}
