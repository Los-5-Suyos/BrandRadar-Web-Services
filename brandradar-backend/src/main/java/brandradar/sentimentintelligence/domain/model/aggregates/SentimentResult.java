package brandradar.sentimentintelligence.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class SentimentResult {

    private final Long id;
    private final Long sentimentAnalysisId;
    private final Long mentionId;
    private final BigDecimal scorePositive;
    private final BigDecimal scoreNegative;
    private final BigDecimal scoreNeutral;
    private final BigDecimal scoreCompound;
    private final String dominantEmotion;
    private final String language;
    private final Instant analyzedAt;

    private SentimentResult(Long id, Long sentimentAnalysisId, Long mentionId, BigDecimal scorePositive, BigDecimal scoreNegative, BigDecimal scoreNeutral, BigDecimal scoreCompound, String dominantEmotion, String language, Instant analyzedAt) {
        this.id = id;
        this.sentimentAnalysisId = Objects.requireNonNull(sentimentAnalysisId, "SentimentAnalysisId is required");
        this.mentionId = Objects.requireNonNull(mentionId, "MentionId is required");
        this.scorePositive = scorePositive != null ? scorePositive : BigDecimal.ZERO;
        this.scoreNegative = scoreNegative != null ? scoreNegative : BigDecimal.ZERO;
        this.scoreNeutral = scoreNeutral != null ? scoreNeutral : BigDecimal.ZERO;
        this.scoreCompound = scoreCompound != null ? scoreCompound : BigDecimal.ZERO;
        this.dominantEmotion = dominantEmotion;
        this.language = language;
        this.analyzedAt = analyzedAt;
    }

    public static SentimentResult create(Long sentimentAnalysisId, Long mentionId, BigDecimal scorePositive, BigDecimal scoreNegative, BigDecimal scoreNeutral, BigDecimal scoreCompound, String dominantEmotion, String language) {
        return new SentimentResult(null, sentimentAnalysisId, mentionId, scorePositive, scoreNegative, scoreNeutral, scoreCompound, dominantEmotion, language, null);
    }

    public static SentimentResult rehydrate(Long id, Long sentimentAnalysisId, Long mentionId, BigDecimal scorePositive, BigDecimal scoreNegative, BigDecimal scoreNeutral, BigDecimal scoreCompound, String dominantEmotion, String language, Instant analyzedAt) {
        return new SentimentResult(id, sentimentAnalysisId, mentionId, scorePositive, scoreNegative, scoreNeutral, scoreCompound, dominantEmotion, language, analyzedAt);
    }

    public Long getId() { return id; }
    public Long getSentimentAnalysisId() { return sentimentAnalysisId; }
    public Long getMentionId() { return mentionId; }
    public BigDecimal getScorePositive() { return scorePositive; }
    public BigDecimal getScoreNegative() { return scoreNegative; }
    public BigDecimal getScoreNeutral() { return scoreNeutral; }
    public BigDecimal getScoreCompound() { return scoreCompound; }
    public String getDominantEmotion() { return dominantEmotion; }
    public String getLanguage() { return language; }
    public Instant getAnalyzedAt() { return analyzedAt; }
}