package brandradar.shared.infrastructure.security;

import brandradar.brandworkspace.domain.model.aggregates.Brand;
import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * El rol ADMIN se salta esta validación (acceso de soporte/back-office).
 */
@Component
public class OwnershipGuard {

    private final BrandWorkspaceRepository workspaceRepository;
    private final BrandRepository brandRepository;
    private final CurrentUser currentUser;

    public OwnershipGuard(BrandWorkspaceRepository workspaceRepository,
                          BrandRepository brandRepository,
                          CurrentUser currentUser) {
        this.workspaceRepository = workspaceRepository;
        this.brandRepository = brandRepository;
        this.currentUser = currentUser;
    }

    /**
     * Verifica que el workspace exista y le pertenezca al usuario que llama.
     * @return el workspace, para reusarlo sin ir dos veces a la BD.
     */
    public BrandWorkspace assertWorkspaceOwnership(Long workspaceId) {
        var user = currentUser.get();
        var workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace not found"));

        if ("ADMIN".equals(user.role())) {
            return workspace;
        }

        if (!workspace.getUserId().equals(user.userId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this workspace");
        }
        return workspace;
    }

    /**
     * Verifica que el brand exista y su workspace le pertenezca al usuario que llama.
     * @return el brand, para reusarlo sin ir dos veces a la BD.
     */
    public Brand assertBrandOwnership(Long brandId) {
        var brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));
        assertWorkspaceOwnership(brand.getWorkspaceId());
        return brand;
    }
}