package brandradar.shared.exception;

public class UnauthorizedWorkspaceAccessException extends RuntimeException {
    public UnauthorizedWorkspaceAccessException(String message) {
        super(message);
    }
}
