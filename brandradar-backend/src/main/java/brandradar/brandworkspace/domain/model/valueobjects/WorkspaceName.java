package brandradar.brandworkspace.domain.model.valueobjects;

public record WorkspaceName(String value) {
    public WorkspaceName {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Workspace name cannot be blank");
        if (value.length() > 255)
            throw new IllegalArgumentException("Workspace name cannot exceed 255 characters");
    }
}