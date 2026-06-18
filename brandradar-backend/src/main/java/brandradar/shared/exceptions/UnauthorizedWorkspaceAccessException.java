package brandradar.shared.exceptions;

public class UnauthorizedWorkspaceAccessException extends RuntimeException {

    public UnauthorizedWorkspaceAccessException(Long workspaceId, Long userId) {
        super("User " + userId + " is not authorized to access workspace " + workspaceId);
    }

    public UnauthorizedWorkspaceAccessException(String message) {
        super(message);
    }
}
