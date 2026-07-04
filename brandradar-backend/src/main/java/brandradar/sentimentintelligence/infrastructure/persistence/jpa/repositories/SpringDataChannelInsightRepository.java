package brandradar.sentimentintelligence.infrastructure.persistence.jpa.repositories;

import brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities.ChannelInsightJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataChannelInsightRepository extends JpaRepository<ChannelInsightJpaEntity, Long> {
    Optional<ChannelInsightJpaEntity> findByBrandIdAndChannelType(Long brandId, String channelType);
    List<ChannelInsightJpaEntity> findByBrandId(Long brandId);
}