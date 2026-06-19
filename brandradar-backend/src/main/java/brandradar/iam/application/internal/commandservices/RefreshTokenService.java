package brandradar.iam.application.internal.commandservices;

import brandradar.iam.application.internal.commandservices.dto.TokenRefreshResult;
import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.repositories.UserAccountRepository;
import brandradar.iam.infrastructure.security.jwt.JwtTokenProvider; // Importación exacta de tu ruta

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class RefreshTokenService {

    private final UserAccountRepository userAccountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenService(UserAccountRepository userAccountRepository, JwtTokenProvider jwtTokenProvider) {
        this.userAccountRepository = userAccountRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * T-09: Valida y rota tokens de acceso utilizando el refresh token de forma segura.
     */
    public TokenRefreshResult refreshResult(String refreshToken) {

        // 1. Validar que el refresh token esté firmado correctamente y no haya expirado
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            log.warn("RefreshTokenService - El token enviado es inválido o expiró.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido o expirado");
        }

        // 2. Extraer el userId del cuerpo interno del refresh token mediante el método real de tu equipo
        Long userId = jwtTokenProvider.extractUserId(refreshToken);

        // 3. Buscar el UserAccount actual en la base de datos
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        // 4. Control de Seguridad de Estado de la Cuenta
        if (!user.isActive()) {
            log.warn("RefreshTokenService - Intento de refresh para cuenta no activa o bloqueada. userId={}", userId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La sesión no es válida para el estado actual del usuario.");
        }

        Long tokenVersion = jwtTokenProvider.extractSessionVersion(refreshToken); // Necesitas este método en JwtTokenProvider

        if (!user.getSessionVersion().equals(tokenVersion)) {
            log.warn("RefreshTokenService - Versión de sesión no coincide! Token: {}, DB: {}", tokenVersion, user.getSessionVersion());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido o expirado");
        }

        // 5. Generar un nuevo par de tokens respetando los parámetros exactos (email de Value Object y userId)
        // Nota: Si user.getEmail().value() te marca error, cámbialo por la propiedad que use tu clase Email (ej. user.getEmail().getEmailAddress())
        String userEmailStr = user.getEmail().value();

        String newAccessToken = jwtTokenProvider.generateToken(userEmailStr, user.getId());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userEmailStr, user.getId(), user.getSessionVersion());

        log.info("RefreshTokenService - Rotación exitosa de tokens para el userId={}", userId);

        return new TokenRefreshResult(newAccessToken, newRefreshToken);
    }
}