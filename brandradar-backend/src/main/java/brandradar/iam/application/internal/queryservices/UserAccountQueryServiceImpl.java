package brandradar.iam.application.internal.queryservices;

import brandradar.iam.application.queries.GetAllUserAccountsQuery;
import brandradar.iam.application.queries.GetUserAccountByIdQuery;
import brandradar.iam.application.queryservices.UserAccountQueryService;
import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.repositories.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountQueryServiceImpl implements UserAccountQueryService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountQueryServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Optional<UserAccount> handle(GetUserAccountByIdQuery query) {
        return userAccountRepository.findById(query.id());
    }

    @Override
    public List<UserAccount> handle(GetAllUserAccountsQuery query) {
        return userAccountRepository.findAll();
    }
}