package brandradar.iam.application.commands;

public record ForgotPasswordCommand(String email) {
    public ForgotPasswordCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }
}