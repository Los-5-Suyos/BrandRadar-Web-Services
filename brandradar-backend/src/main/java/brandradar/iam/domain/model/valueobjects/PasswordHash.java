package brandradar.iam.domain.model.valueobjects;

public record PasswordHash(String value) {
    public PasswordHash {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Password hash cannot be blank");
    }
}