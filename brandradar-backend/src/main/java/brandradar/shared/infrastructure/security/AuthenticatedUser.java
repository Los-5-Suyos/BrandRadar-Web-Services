package brandradar.shared.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuthenticatedUser {

    private AuthenticatedUser() {}

    public static Optional<Long> getId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        return authentication.getPrincipal() instanceof Long userId ? Optional.of(userId) : Optional.empty();
    }
}
