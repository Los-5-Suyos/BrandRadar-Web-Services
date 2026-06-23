package brandradar.iam.application.commands;

import brandradar.iam.domain.model.valueobjects.Email;

public record VerifyAccountCommand(Email email, String code) {}