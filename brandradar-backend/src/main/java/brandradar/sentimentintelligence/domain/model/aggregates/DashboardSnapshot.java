package brandradar.sentimentintelligence.domain.model.aggregates;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardSnapshot {

    private final Long id;
    private final Long brandId;
    private final LocalDate date;
    private final BigDecimal sentimentScore;
    private final Long mentionsCount;
    private final BigDecimal positivePct;
    private final BigDecimal neutralPct;
    private final BigDecimal negativePct;
    private final String crisisAnalysisText;

    private DashboardSnapshot(Long id, Long brandId, LocalDate date, BigDecimal sentimentScore,
                              Long mentionsCount, BigDecimal positivePct, BigDecimal neutralPct,
                              BigDecimal negativePct, String crisisAnalysisText) {
        this.id = id;
        this.brandId = brandId;
        this.date = date;
        this.sentimentScore = sentimentScore;
        this.mentionsCount = mentionsCount;
        this.positivePct = positivePct;
        this.neutralPct = neutralPct;
        this.negativePct = negativePct;
        this.crisisAnalysisText = crisisAnalysisText;
    }

    public static DashboardSnapshot create(Long brandId, LocalDate date, BigDecimal sentimentScore,
                                           Long mentionsCount, BigDecimal positivePct,
                                           BigDecimal neutralPct, BigDecimal negativePct,
                                           String crisisAnalysisText) {
        return new DashboardSnapshot(null, brandId, date, sentimentScore, mentionsCount,
                positivePct, neutralPct, negativePct, crisisAnalysisText);
    }

    public static DashboardSnapshot rehydrate(Long id, Long brandId, LocalDate date, BigDecimal sentimentScore,
                                              Long mentionsCount, BigDecimal positivePct,
                                              BigDecimal neutralPct, BigDecimal negativePct,
                                              String crisisAnalysisText) {
        return new DashboardSnapshot(id, brandId, date, sentimentScore, mentionsCount,
                positivePct, neutralPct, negativePct, crisisAnalysisText);
    }

    public DashboardSnapshot withValues(BigDecimal sentimentScore, Long mentionsCount,
                                        BigDecimal positivePct, BigDecimal neutralPct,
                                        BigDecimal negativePct, String crisisAnalysisText) {
        return new DashboardSnapshot(this.id, this.brandId, this.date, sentimentScore,
                mentionsCount, positivePct, neutralPct, negativePct, crisisAnalysisText);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public LocalDate getDate() { return date; }
    public BigDecimal getSentimentScore() { return sentimentScore; }
    public Long getMentionsCount() { return mentionsCount; }
    public BigDecimal getPositivePct() { return positivePct; }
    public BigDecimal getNeutralPct() { return neutralPct; }
    public BigDecimal getNegativePct() { return negativePct; }
    public String getCrisisAnalysisText() { return crisisAnalysisText; }
}