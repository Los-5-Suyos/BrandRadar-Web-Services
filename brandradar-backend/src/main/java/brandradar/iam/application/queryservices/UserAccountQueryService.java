package brandradar.iam.application.queryservices;

import brandradar.iam.application.queries.GetAllUserAccountsQuery;
import brandradar.iam.application.queries.GetUserAccountByIdQuery;
import brandradar.iam.domain.model.aggregates.UserAccount;

import java.util.List;
import java.util.Optional;

public interface UserAccountQueryService {
    Optional<UserAccount> handle(GetUserAccountByIdQuery query);
    List<UserAccount> handle(GetAllUserAccountsQuery query);
}