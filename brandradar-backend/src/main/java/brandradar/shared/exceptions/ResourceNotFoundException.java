package brandradar.shared.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object identifier) {
        super(resource + " not found with identifier: " + identifier);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
