package brandradar.shared.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    /**
     * @return el AuthenticatedUser resuelto desde el JWT de la petición actual.
     */
    public AuthenticatedUser get() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalStateException("No authenticated user in security context");
        }
        return user;
    }
}