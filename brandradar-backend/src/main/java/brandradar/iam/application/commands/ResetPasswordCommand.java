package brandradar.iam.application.commands;

public record ResetPasswordCommand(
        String token,
        String newPassword
) {
    public ResetPasswordCommand {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is required");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }
    }
}