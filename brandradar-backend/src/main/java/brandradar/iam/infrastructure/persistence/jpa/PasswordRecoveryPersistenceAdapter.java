package brandradar.iam.infrastructure.persistence.jpa;

import brandradar.iam.domain.model.aggregates.PasswordRecovery;
import brandradar.iam.domain.model.repositories.PasswordRecoveryRepository;
import brandradar.iam.infrastructure.persistence.jpa.mappers.PasswordRecoveryPersistenceMapper;
import brandradar.iam.infrastructure.persistence.jpa.repositories.SpringDataPasswordRecoveryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PasswordRecoveryPersistenceAdapter implements PasswordRecoveryRepository {

    private final SpringDataPasswordRecoveryRepository springDataRepository;

    public PasswordRecoveryPersistenceAdapter(SpringDataPasswordRecoveryRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public PasswordRecovery save(PasswordRecovery recovery) {
        var jpaEntity = PasswordRecoveryPersistenceMapper.toJpaEntity(recovery);
        var saved = springDataRepository.save(jpaEntity);
        return PasswordRecoveryPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<PasswordRecovery> findByToken(String token) {
        return springDataRepository.findByToken(token)
                .map(PasswordRecoveryPersistenceMapper::toDomain);
    }
}