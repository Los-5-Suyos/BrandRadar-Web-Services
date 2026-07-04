package brandradar.crisisdetection.infrastructure.persistence.jpa;

import brandradar.crisisdetection.domain.model.aggregates.CrisisAnalysisLog;
import brandradar.crisisdetection.domain.model.repositories.CrisisAnalysisLogRepository;
import brandradar.crisisdetection.infrastructure.persistence.jpa.mappers.CrisisAnalysisLogPersistenceMapper;
import brandradar.crisisdetection.infrastructure.persistence.jpa.repositories.SpringDataCrisisAnalysisLogRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CrisisAnalysisLogPersistenceAdapter implements CrisisAnalysisLogRepository {

    private final SpringDataCrisisAnalysisLogRepository springDataRepository;

    public CrisisAnalysisLogPersistenceAdapter(SpringDataCrisisAnalysisLogRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public CrisisAnalysisLog save(CrisisAnalysisLog log) {
        var jpaEntity = CrisisAnalysisLogPersistenceMapper.toJpaEntity(log);
        var saved = springDataRepository.save(jpaEntity);
        return CrisisAnalysisLogPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<CrisisAnalysisLog> findByIncidentIdOrderByCreatedAtDesc(Long incidentId) {
        return springDataRepository.findByIncidentIdOrderByCreatedAtDesc(incidentId)
                .stream()
                .map(CrisisAnalysisLogPersistenceMapper::toDomain)
                .toList();
    }
}