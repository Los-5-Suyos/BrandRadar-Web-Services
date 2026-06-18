package brandradar.iam.application.queryservices;

import brandradar.iam.application.queries.GetAllUserAccountsQuery;
import brandradar.iam.application.queries.GetUserAccountByIdQuery;
import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.valueobjects.Email;

import java.util.List;
import java.util.Optional;

public interface UserAccountQueryService {
    List<UserAccount> handle(GetAllUserAccountsQuery query);
    Optional<UserAccount> handle(GetUserAccountByIdQuery query);
    Optional<UserAccount> findByEmail(Email email);
}