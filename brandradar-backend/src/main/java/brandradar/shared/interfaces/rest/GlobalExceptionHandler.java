package brandradar.shared.interfaces.rest;

import brandradar.shared.exceptions.DomainValidationException;
import brandradar.shared.exceptions.ResourceNotFoundException;
import brandradar.shared.exceptions.TokenExpiredException;
import brandradar.shared.exceptions.UnauthorizedWorkspaceAccessException;
import brandradar.shared.interfaces.rest.resources.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 — Validación de negocio
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResource> handleDomainValidation(DomainValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResource.of(400, "VALIDATION_ERROR", ex.getMessage()));
    }

    // 400 — Errores de validación @Valid en Request Bodies
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResource> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse("Validation error");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResource.of(400, "VALIDATION_ERROR", message));
    }

    // 403 — Acceso no autorizado a workspace ajeno
    @ExceptionHandler(UnauthorizedWorkspaceAccessException.class)
    public ResponseEntity<ErrorResource> handleUnauthorizedWorkspaceAccess(UnauthorizedWorkspaceAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResource.of(403, "ACCESS_DENIED", ex.getMessage()));
    }

    // 404 — Recurso no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResource> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResource.of(404, "NOT_FOUND", ex.getMessage()));
    }

    // 410 — Token expirado o ya usado
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResource> handleTokenExpired(TokenExpiredException ex) {
        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(ErrorResource.of(410, "TOKEN_EXPIRED", ex.getMessage()));
    }

    // 500 — Error genérico inesperado
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResource> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResource.of(500, "INTERNAL_ERROR", "An unexpected error occurred."));
    }
}