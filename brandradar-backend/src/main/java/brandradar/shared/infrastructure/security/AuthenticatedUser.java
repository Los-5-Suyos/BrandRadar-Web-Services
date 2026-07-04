package brandradar.shared.infrastructure.security;
 
public record AuthenticatedUser(Long userId, String email, String role) {
}