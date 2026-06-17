package brandradar.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordResource(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {}