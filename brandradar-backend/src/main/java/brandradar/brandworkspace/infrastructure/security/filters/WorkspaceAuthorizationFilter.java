package brandradar.brandworkspace.infrastructure.security.filters;

import brandradar.brandworkspace.domain.model.aggregates.BrandWorkspace;
import brandradar.brandworkspace.domain.model.aggregates.WorkspaceAccessAudit;
import brandradar.brandworkspace.domain.model.repositories.BrandWorkspaceRepository;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceAccessAuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class WorkspaceAuthorizationFilter extends OncePerRequestFilter {

    private final BrandWorkspaceRepository brandWorkspaceRepository;
    private final WorkspaceAccessAuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    public WorkspaceAuthorizationFilter(BrandWorkspaceRepository brandWorkspaceRepository,
                                        WorkspaceAccessAuditRepository auditRepository,
                                        ObjectMapper objectMapper) {
        this.brandWorkspaceRepository = brandWorkspaceRepository;
        this.auditRepository = auditRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        Long workspaceId = extractWorkspaceId(path);

        if (workspaceId != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() instanceof Long) {
                Long userId = (Long) authentication.getCredentials();

                Optional<BrandWorkspace> workspaceOpt = brandWorkspaceRepository.findById(workspaceId);

                if (workspaceOpt.isEmpty() || !workspaceOpt.get().getUserId().equals(userId)) {
                    // No existe o no le pertenece: Log de auditoría
                    auditRepository.save(WorkspaceAccessAudit.create(workspaceId, userId, request.getRemoteAddr()));
                    
                    // Retornar 403 Forbidden
                    handleUnauthorized(response);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private Long extractWorkspaceId(String path) {
        try {
            if (path.startsWith("/api/v1/workspaces/")) {
                String[] parts = path.split("/");
                // path is /api/v1/workspaces/1 => ["", "api", "v1", "workspaces", "1"]
                if (parts.length >= 5) {
                    return Long.parseLong(parts[4]);
                }
            } else if (path.startsWith("/api/v1/dashboard/workspace/")) {
                String[] parts = path.split("/");
                // path is /api/v1/dashboard/workspace/1 => ["", "api", "v1", "dashboard", "workspace", "1"]
                if (parts.length >= 6) {
                    return Long.parseLong(parts[5]);
                }
            }
        } catch (NumberFormatException e) {
            // No es un ID numérico válido, ignoramos
        }
        return null;
    }

    private void handleUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", java.time.Instant.now().toString());
        errorDetails.put("status", 403);
        errorDetails.put("error", "Forbidden");
        errorDetails.put("message", "You do not have permission to access this workspace");

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
