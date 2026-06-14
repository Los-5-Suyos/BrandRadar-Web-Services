package brandradar.iam.interfaces.rest.transform;

import brandradar.iam.application.commands.CreateUserAccountCommand;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.domain.model.valueobjects.PasswordHash;
import brandradar.iam.interfaces.rest.resources.CreateUserAccountResource;

public class CreateUserAccountCommandFromResourceAssembler {

    private CreateUserAccountCommandFromResourceAssembler() {}

    public static CreateUserAccountCommand toCommand(CreateUserAccountResource resource) {
        return new CreateUserAccountCommand(
                new Email(resource.email()),
                new PasswordHash(resource.password()),
                resource.role(),
                resource.description()
        );
    }
}