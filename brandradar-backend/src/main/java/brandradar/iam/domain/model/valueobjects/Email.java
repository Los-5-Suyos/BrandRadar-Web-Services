package brandradar.iam.domain.model.valueobjects;

public record Email(String value) {
    public Email {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Email cannot be blank");
        if (!value.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Email format is invalid");
    }
}