package brandradar.iam.interfaces.rest.resources;

public record LoginResponse(
        String token,
        String refreshToken,
        Long userId,
        String email,
        String role
) {}