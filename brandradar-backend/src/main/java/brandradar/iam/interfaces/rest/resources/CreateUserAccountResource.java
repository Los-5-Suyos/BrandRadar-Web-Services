package brandradar.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserAccountResource(
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank @Pattern(regexp = "ADMIN|ANALYST|VIEWER") String role,
        String description
) {}