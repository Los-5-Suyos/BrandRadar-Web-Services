package brandradar.iam.application.commandservices;

import brandradar.iam.application.commands.LoginCommand;
import brandradar.iam.interfaces.rest.resources.LoginResponse;

public interface LoginService {
    LoginResponse handle(LoginCommand command);
}
