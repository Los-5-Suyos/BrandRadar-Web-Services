package brandradar.sentimentintelligence.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class SentimentAnalysis {

    private final Long id;
    private final Long brandId;
    private final Instant periodFrom;
    private final Instant periodTo;
    private final BigDecimal scorePositive;
    private final BigDecimal scoreNegative;
    private final BigDecimal scoreNeutral;
    private final BigDecimal scoreCompound;
    private final String trendDirection;
    private final BigDecimal trendMagnitude;
    private final BigDecimal deltaPreviousScore;
    private final BigDecimal deltaCurrentScore;
    private final BigDecimal deltaChangePct;
    private final Instant createdAt;

    private SentimentAnalysis(Long id, Long brandId, Instant periodFrom, Instant periodTo, BigDecimal scorePositive, BigDecimal scoreNegative, BigDecimal scoreNeutral, BigDecimal scoreCompound, String trendDirection, BigDecimal trendMagnitude, BigDecimal deltaPreviousScore, BigDecimal deltaCurrentScore, BigDecimal deltaChangePct, Instant createdAt) {
        this.id = id;
        this.brandId = Objects.requireNonNull(brandId, "BrandId is required");
        this.periodFrom = Objects.requireNonNull(periodFrom, "PeriodFrom is required");
        this.periodTo = Objects.requireNonNull(periodTo, "PeriodTo is required");
        this.scorePositive = scorePositive != null ? scorePositive : BigDecimal.ZERO;
        this.scoreNegative = scoreNegative != null ? scoreNegative : BigDecimal.ZERO;
        this.scoreNeutral = scoreNeutral != null ? scoreNeutral : BigDecimal.ZERO;
        this.scoreCompound = scoreCompound != null ? scoreCompound : BigDecimal.ZERO;
        this.trendDirection = trendDirection;
        this.trendMagnitude = trendMagnitude != null ? trendMagnitude : BigDecimal.ZERO;
        this.deltaPreviousScore = deltaPreviousScore != null ? deltaPreviousScore : BigDecimal.ZERO;
        this.deltaCurrentScore = deltaCurrentScore != null ? deltaCurrentScore : BigDecimal.ZERO;
        this.deltaChangePct = deltaChangePct != null ? deltaChangePct : BigDecimal.ZERO;
        this.createdAt = createdAt;
    }

    public static SentimentAnalysis create(Long brandId, Instant periodFrom, Instant periodTo) {
        return new SentimentAnalysis(null, brandId, periodFrom, periodTo, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null);
    }

    public static SentimentAnalysis rehydrate(Long id, Long brandId, Instant periodFrom, Instant periodTo, BigDecimal scorePositive, BigDecimal scoreNegative, BigDecimal scoreNeutral, BigDecimal scoreCompound, String trendDirection, BigDecimal trendMagnitude, BigDecimal deltaPreviousScore, BigDecimal deltaCurrentScore, BigDecimal deltaChangePct, Instant createdAt) {
        return new SentimentAnalysis(id, brandId, periodFrom, periodTo, scorePositive, scoreNegative, scoreNeutral, scoreCompound, trendDirection, trendMagnitude, deltaPreviousScore, deltaCurrentScore, deltaChangePct, createdAt);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public Instant getPeriodFrom() { return periodFrom; }
    public Instant getPeriodTo() { return periodTo; }
    public BigDecimal getScorePositive() { return scorePositive; }
    public BigDecimal getScoreNegative() { return scoreNegative; }
    public BigDecimal getScoreNeutral() { return scoreNeutral; }
    public BigDecimal getScoreCompound() { return scoreCompound; }
    public String getTrendDirection() { return trendDirection; }
    public BigDecimal getTrendMagnitude() { return trendMagnitude; }
    public BigDecimal getDeltaPreviousScore() { return deltaPreviousScore; }
    public BigDecimal getDeltaCurrentScore() { return deltaCurrentScore; }
    public BigDecimal getDeltaChangePct() { return deltaChangePct; }
    public Instant getCreatedAt() { return createdAt; }
}