package brandradar.brandworkspace.domain.model.repositories;

import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;

import java.util.List;
import java.util.Optional;

public interface BrandWorkspaceRepository {
    BrandWorkspace save(BrandWorkspace workspace);
    Optional<BrandWorkspace> findById(Long id);
    List<BrandWorkspace> findByUserId(Long userId);
    List<BrandWorkspace> findAll();
    void deleteById(Long id);
}