package brandradar.iam.interfaces.rest.resources;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        int expiresIn
) {}
