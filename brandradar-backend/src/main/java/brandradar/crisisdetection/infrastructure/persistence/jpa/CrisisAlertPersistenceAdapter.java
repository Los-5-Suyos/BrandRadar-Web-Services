package brandradar.crisisdetection.infrastructure.persistence.jpa;

import brandradar.crisisdetection.domain.model.aggregates.CrisisAlert;
import brandradar.crisisdetection.domain.model.repositories.CrisisAlertRepository;
import brandradar.crisisdetection.infrastructure.persistence.jpa.mappers.CrisisAlertPersistenceMapper;
import brandradar.crisisdetection.infrastructure.persistence.jpa.repositories.SpringDataCrisisAlertRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CrisisAlertPersistenceAdapter implements CrisisAlertRepository {

    private final SpringDataCrisisAlertRepository springDataRepository;

    public CrisisAlertPersistenceAdapter(SpringDataCrisisAlertRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public CrisisAlert save(CrisisAlert alert) {
        var jpaEntity = CrisisAlertPersistenceMapper.toJpaEntity(alert);
        var saved = springDataRepository.save(jpaEntity);
        return CrisisAlertPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<CrisisAlert> findById(Long id) {
        return springDataRepository.findById(id)
                .map(CrisisAlertPersistenceMapper::toDomain);
    }

    @Override
    public List<CrisisAlert> findByBrandId(Long brandId) {
        return springDataRepository.findByBrandId(brandId)
                .stream()
                .map(CrisisAlertPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<CrisisAlert> findByStatus(String status) {
        return springDataRepository.findByStatus(status)
                .stream()
                .map(CrisisAlertPersistenceMapper::toDomain)
                .toList();
    }
}