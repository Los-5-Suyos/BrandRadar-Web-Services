package brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SentimentResult")
public class SentimentResultJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SER_id")
    private Long id;

    @Column(name = "SEA_id", nullable = false)
    private Long sentimentAnalysisId;

    @Column(name = "MEN_id", nullable = false)
    private Long mentionId;

    @Column(name = "SER_score_positive", nullable = false)
    private BigDecimal scorePositive;

    @Column(name = "SER_score_negative", nullable = false)
    private BigDecimal scoreNegative;

    @Column(name = "SER_score_neutral", nullable = false)
    private BigDecimal scoreNeutral;

    @Column(name = "SER_score_compound", nullable = false)
    private BigDecimal scoreCompound;

    @Column(name = "SER_dominant_emotion", length = 20)
    private String dominantEmotion;

    @Column(name = "SER_language", length = 10)
    private String language;

    @CreatedDate
    @Column(name = "SER_analyzed_at", nullable = false, updatable = false)
    private Instant analyzedAt;

    public SentimentResultJpaEntity(Long id, Long sentimentAnalysisId, Long mentionId, BigDecimal scorePositive, BigDecimal scoreNegative, BigDecimal scoreNeutral, BigDecimal scoreCompound, String dominantEmotion, String language) {
        this.id = id;
        this.sentimentAnalysisId = sentimentAnalysisId;
        this.mentionId = mentionId;
        this.scorePositive = scorePositive;
        this.scoreNegative = scoreNegative;
        this.scoreNeutral = scoreNeutral;
        this.scoreCompound = scoreCompound;
        this.dominantEmotion = dominantEmotion;
        this.language = language;
    }
}