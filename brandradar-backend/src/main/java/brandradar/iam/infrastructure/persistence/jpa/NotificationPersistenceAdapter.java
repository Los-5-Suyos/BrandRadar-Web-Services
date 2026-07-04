package brandradar.iam.infrastructure.persistence.jpa;

import brandradar.iam.domain.model.aggregates.Notification;
import brandradar.iam.domain.model.repositories.NotificationRepository;
import brandradar.iam.infrastructure.persistence.jpa.mappers.NotificationPersistenceMapper;
import brandradar.iam.infrastructure.persistence.jpa.repositories.SpringDataNotificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationPersistenceAdapter implements NotificationRepository {

    private final SpringDataNotificationRepository springDataRepository;

    public NotificationPersistenceAdapter(SpringDataNotificationRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Notification save(Notification notification) {
        var jpaEntity = NotificationPersistenceMapper.toJpaEntity(notification);
        var saved = springDataRepository.save(jpaEntity);
        return NotificationPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return springDataRepository.findById(id)
                .map(NotificationPersistenceMapper::toDomain);
    }

    @Override
    public List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return springDataRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public long countByUserIdAndIsReadFalse(Long userId) {
        return springDataRepository.countByUserIdAndIsReadFalse(userId);
    }
}