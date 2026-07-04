package brandradar.reputationmonitoring.application.services;

import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataKeywordRuleRepository;
import brandradar.crisisdetection.infrastructure.groq.GroqApiClient;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.sentimentintelligence.application.services.SentimentScoreCalculator;
import brandradar.sentimentintelligence.domain.model.repositories.DashboardSnapshotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportGenerationService {

    private static final ZoneId ZONE = ZoneId.of("America/Lima");

    private static final String SENTIMENT_SCORE_EXPLANATION =
            "Score de 0 a 100 calculado con IA sobre el contenido de cada mención (0=muy negativo, 100=muy positivo).";
    private static final String MENTIONS_EXPLANATION =
            "Total de comentarios/posts/videos encontrados que mencionan la marca en el periodo, en las 4 " +
                    "plataformas conectadas (YouTube, Twitter, Reddit, TikTok).";
    private static final String REACH_EXPLANATION =
            "Suma de vistas reales del contenido relacionado a la marca. Se calcula solo con YouTube y TikTok " +
                    "(sí exponen conteo de vistas); Twitter y Reddit no aportan a este número porque esas plataformas " +
                    "no proveen datos de vistas confiables en este momento — el reach real es mayor al mostrado aquí.";

    private final MentionRepository mentionRepository;
    private final SentimentScoreCalculator sentimentScoreCalculator;
    private final SpringDataKeywordRuleRepository keywordRuleRepository;
    private final DashboardSnapshotRepository dashboardSnapshotRepository;
    private final GroqApiClient groqApiClient;

    public ReportGenerationService(MentionRepository mentionRepository,
                                   SentimentScoreCalculator sentimentScoreCalculator,
                                   SpringDataKeywordRuleRepository keywordRuleRepository,
                                   DashboardSnapshotRepository dashboardSnapshotRepository,
                                   GroqApiClient groqApiClient) {
        this.mentionRepository = mentionRepository;
        this.sentimentScoreCalculator = sentimentScoreCalculator;
        this.keywordRuleRepository = keywordRuleRepository;
        this.dashboardSnapshotRepository = dashboardSnapshotRepository;
        this.groqApiClient = groqApiClient;
    }

    public ReportData generate(Long brandId, String brandName, LocalDate periodFrom, LocalDate periodTo) {
        var allMentions = mentionRepository.findByBrandId(brandId);
        var currentPeriod = filterByDateRange(allMentions, periodFrom, periodTo);

        long daysInPeriod = ChronoUnit.DAYS.between(periodFrom, periodTo) + 1;
        var previousFrom = periodFrom.minusDays(daysInPeriod);
        var previousTo = periodFrom.minusDays(1);
        var previousPeriod = filterByDateRange(allMentions, previousFrom, previousTo);

        var currentScore = sentimentScoreCalculator.calculateForBrand(brandId, brandName, currentPeriod);
        Double scoreDeltaPercent = null;
        if (!previousPeriod.isEmpty()) {
            var previousScore = sentimentScoreCalculator.calculateForBrand(brandId, brandName, previousPeriod);
            if (previousScore.doubleValue() > 0) {
                scoreDeltaPercent = (currentScore.doubleValue() - previousScore.doubleValue())
                        / previousScore.doubleValue() * 100;
            }
        }

        long totalMentions = currentPeriod.size();
        Double mentionsDeltaPercent = previousPeriod.isEmpty() ? null
                : ((double) (totalMentions - previousPeriod.size()) / previousPeriod.size()) * 100;

        long reach = currentPeriod.stream()
                .mapToLong(m -> m.getEngagementViews() != null ? m.getEngagementViews() : 0)
                .sum();
        long previousReach = previousPeriod.stream()
                .mapToLong(m -> m.getEngagementViews() != null ? m.getEngagementViews() : 0)
                .sum();
        Double reachDeltaPercent = previousReach == 0 ? null
                : ((double) (reach - previousReach) / previousReach) * 100;

        var evolution = buildSentimentEvolution(brandId, periodFrom, periodTo);
        var topKeywords = buildTopKeywords(brandId, currentPeriod);
        var criticalAccounts = buildCriticalAccounts(currentPeriod);

        String executiveSummary = generateExecutiveSummary(brandName, currentScore.doubleValue(),
                totalMentions, currentPeriod, topKeywords);

        return new ReportData(
                brandName, periodFrom, periodTo,
                executiveSummary,
                currentScore.doubleValue(), round1(scoreDeltaPercent), SENTIMENT_SCORE_EXPLANATION,
                totalMentions, round1(mentionsDeltaPercent), MENTIONS_EXPLANATION,
                reach, round1(reachDeltaPercent), REACH_EXPLANATION,
                evolution, topKeywords, criticalAccounts
        );
    }

    /**
     * Genera una frase/párrafo con IA que resuma en lenguaje natural cómo está la reputación
     * de la marca — "qué piensa la gente" — con fallback a texto por reglas si Groq falla.
     */
    private String generateExecutiveSummary(String brandName, double score, long totalMentions,
                                            List<Mention> currentPeriod, List<ReportData.KeywordScore> topKeywords) {
        try {
            long negative = currentPeriod.stream()
                    .filter(m -> m.getSentimentCompound() != null && m.getSentimentCompound().doubleValue() < -0.3)
                    .count();
            long positive = currentPeriod.stream()
                    .filter(m -> m.getSentimentCompound() != null && m.getSentimentCompound().doubleValue() > 0.3)
                    .count();

            String topKeywordNames = topKeywords.stream()
                    .limit(3)
                    .map(ReportData.KeywordScore::keyword)
                    .collect(Collectors.joining(", "));

            String prompt = String.format("""
                    Con estos datos sobre la marca "%s" en el periodo analizado:
                    - Sentiment score: %.1f/100
                    - Total de menciones: %d (positivas: %d, negativas: %d)
                    - Temas más mencionados: %s

                    Escribe un párrafo breve (máximo 3 oraciones) en español, dirigido al dueño de la marca,
                    resumiendo en lenguaje natural cómo está su reputación y qué percibe la gente sobre ella.
                    Sé directo y específico, no genérico. Responde solo con el párrafo, sin introducción.
                    """, brandName, score, totalMentions, positive, negative,
                    topKeywordNames.isEmpty() ? "sin datos suficientes" : topKeywordNames);

            String result = groqApiClient.chat(prompt);
            if (result == null || result.isBlank()) throw new IllegalStateException("Empty response");
            return result.trim();
        } catch (Exception e) {
            log.warn("ReportGenerationService - Groq failed for executive summary, using fallback: {}", e.getMessage());
            return buildRuleBasedSummary(brandName, score, totalMentions);
        }
    }

    private String buildRuleBasedSummary(String brandName, double score, long totalMentions) {
        String estado = score >= 70 ? "buena" : score >= 45 ? "regular, con áreas de mejora" : "crítica, requiere atención";
        return String.format(
                "%s tiene una reputación %s en el periodo analizado, con un score de %.1f sobre 100 " +
                        "basado en %d menciones recolectadas.", brandName, estado, score, totalMentions);
    }

    private List<Mention> filterByDateRange(List<Mention> mentions, LocalDate from, LocalDate to) {
        return mentions.stream()
                .filter(m -> m.getPublishedAt() != null)
                .filter(m -> {
                    var date = m.getPublishedAt().atZone(ZONE).toLocalDate();
                    return !date.isBefore(from) && !date.isAfter(to);
                })
                .toList();
    }

    private List<ReportData.TrendPoint> buildSentimentEvolution(Long brandId, LocalDate from, LocalDate to) {
        var snapshots = dashboardSnapshotRepository.findLastNDaysByBrandId(brandId, 90);

        return snapshots.stream()
                .filter(s -> !s.getDate().isBefore(from) && !s.getDate().isAfter(to))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .map(s -> new ReportData.TrendPoint(s.getDate(), s.getSentimentScore().doubleValue()))
                .toList();
    }

    private List<ReportData.KeywordScore> buildTopKeywords(Long brandId, List<Mention> periodMentions) {
        var keywords = keywordRuleRepository.findByBrandId(brandId);

        return keywords.stream()
                .map(k -> {
                    var kw = k.getKeyword().toLowerCase(Locale.ROOT);
                    var matching = periodMentions.stream()
                            .filter(m -> m.getContent() != null
                                    && m.getContent().toLowerCase(Locale.ROOT).contains(kw))
                            .toList();
                    if (matching.isEmpty()) return null;

                    double avgCompound = matching.stream()
                            .mapToDouble(m -> m.getSentimentCompound() != null
                                    ? m.getSentimentCompound().doubleValue() : 0.0)
                            .average().orElse(0.0);
                    double score = Math.round(((avgCompound + 1.0) / 2.0 * 5.0) * 10.0) / 10.0;

                    return new ReportData.KeywordScore(k.getKeyword(), matching.size(), score);
                })
                .filter(java.util.Objects::nonNull)
                .sorted((a, b) -> Long.compare(b.count(), a.count()))
                .limit(10)
                .toList();
    }

    private List<ReportData.CriticalAccount> buildCriticalAccounts(List<Mention> periodMentions) {
        var byAuthorPlatform = periodMentions.stream()
                .filter(m -> m.getAuthor() != null && m.getSourcePlatform() != null)
                .collect(Collectors.groupingBy(m -> m.getAuthor() + "||" + m.getSourcePlatform()));

        return byAuthorPlatform.entrySet().stream()
                .map(entry -> {
                    var parts = entry.getKey().split("\\|\\|");
                    var mentions = entry.getValue();
                    double avgCompound = mentions.stream()
                            .mapToDouble(m -> m.getSentimentCompound() != null
                                    ? m.getSentimentCompound().doubleValue() : 0.0)
                            .average().orElse(0.0);
                    return new ReportData.CriticalAccount(parts[0], parts[1], mentions.size(), round1(avgCompound));
                })
                .filter(a -> a.avgSentiment() < 0)
                .sorted((a, b) -> {
                    int byScore = Double.compare(a.avgSentiment(), b.avgSentiment());
                    return byScore != 0 ? byScore : Long.compare(b.mentionsCount(), a.mentionsCount());
                })
                .limit(10)
                .toList();
    }

    private Double round1(Double value) {
        return value != null ? Math.round(value * 10.0) / 10.0 : null;
    }
}