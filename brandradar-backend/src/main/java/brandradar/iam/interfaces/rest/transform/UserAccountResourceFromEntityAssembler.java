package brandradar.iam.interfaces.rest.transform;

import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.interfaces.rest.resources.UserAccountResource;

public class UserAccountResourceFromEntityAssembler {

    private UserAccountResourceFromEntityAssembler() {}

    public static UserAccountResource toResourceFromEntity(UserAccount userAccount) {
        return new UserAccountResource(
                userAccount.getId(),
                userAccount.getEmail().value(),
                userAccount.getRole(),
                userAccount.getDescription(),
                userAccount.getStatus(),
                userAccount.getCreatedAt(),
                userAccount.getUpdatedAt()
        );
    }
}