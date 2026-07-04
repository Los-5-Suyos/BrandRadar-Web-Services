package brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ChannelInsight", uniqueConstraints = @UniqueConstraint(columnNames = {"BRA_id", "CHI_channel_type"}))
public class ChannelInsightJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHI_id")
    private Long id;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;

    @Column(name = "CHI_channel_type", nullable = false, length = 20)
    private String channelType;

    @Column(name = "CHI_insight_text", columnDefinition = "TEXT")
    private String insightText;

    @LastModifiedDate
    @Column(name = "CHI_generated_at", nullable = false)
    private Instant generatedAt;

    public ChannelInsightJpaEntity(Long id, Long brandId, String channelType, String insightText) {
        this.id = id;
        this.brandId = brandId;
        this.channelType = channelType;
        this.insightText = insightText;
    }
}