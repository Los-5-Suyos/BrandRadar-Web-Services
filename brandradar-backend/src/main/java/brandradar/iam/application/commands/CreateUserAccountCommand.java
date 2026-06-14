package brandradar.iam.application.commands;

import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;

public record CreateUserAccountCommand(
        Email email,
        PasswordHash passwordHash,
        String role,
        String description
) {}