package brandradar.brandworkspace.domain.model.repositories;

import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.valueobjects.WorkspaceStatus;

import java.util.List;
import java.util.Optional;

public interface BrandWorkspaceRepository {
    BrandWorkspace save(BrandWorkspace workspace);
    Optional<BrandWorkspace> findById(Long id);
    List<BrandWorkspace> findByUserId(Long userId);
    Optional<BrandWorkspace> findByIdAndUserId(Long id, Long userId);
    List<BrandWorkspace> findByStatus(WorkspaceStatus status);
    List<BrandWorkspace> findAll();
}
