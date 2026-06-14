package brandradar.iam.infrastructure.persistence.jpa;

import brandradar.iam.domain.model.aggregates.UserAccount;
import brandradar.iam.domain.model.repositories.UserAccountRepository;
import brandradar.iam.domain.model.valueobjects.Email;
import brandradar.iam.infrastructure.persistence.jpa.mappers.UserAccountPersistenceMapper;
import brandradar.iam.infrastructure.persistence.jpa.repositories.SpringDataUserAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserAccountPersistenceAdapter implements UserAccountRepository {

    private final SpringDataUserAccountRepository springDataRepository;

    public UserAccountPersistenceAdapter(SpringDataUserAccountRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        var jpaEntity = UserAccountPersistenceMapper.toJpaEntity(userAccount);
        var saved = springDataRepository.save(jpaEntity);
        return UserAccountPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<UserAccount> findById(Long id) {
        return springDataRepository.findById(id)
                .map(UserAccountPersistenceMapper::toDomain);
    }

    @Override
    public Optional<UserAccount> findByEmail(Email email) {
        return springDataRepository.findByEmail(email.value())
                .map(UserAccountPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return springDataRepository.existsByEmail(email.value());
    }

    @Override
    public List<UserAccount> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(UserAccountPersistenceMapper::toDomain)
                .toList();
    }
}