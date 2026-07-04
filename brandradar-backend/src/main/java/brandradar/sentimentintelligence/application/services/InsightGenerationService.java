package brandradar.sentimentintelligence.application.services;

import brandradar.crisisdetection.infrastructure.groq.GroqApiClient;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.sentimentintelligence.domain.model.aggregates.ChannelInsight;
import brandradar.sentimentintelligence.domain.model.repositories.ChannelInsightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Genera insights de texto (por canal + diagnóstico general de crisis) usando Groq,
 * UNA VEZ por refresh — nunca en cada carga de dashboard. Si Groq falla (cuota agotada,
 * caído, timeout), cae automáticamente a un texto simple basado en reglas, para que el
 * dashboard nunca se quede sin insight por un problema de la IA.
 */
@Slf4j
@Service
public class InsightGenerationService {

    private final GroqApiClient groqApiClient;
    private final ChannelInsightRepository channelInsightRepository;

    public InsightGenerationService(GroqApiClient groqApiClient,
                                    ChannelInsightRepository channelInsightRepository) {
        this.groqApiClient = groqApiClient;
        this.channelInsightRepository = channelInsightRepository;
    }

    @Transactional
    public void generateChannelInsights(Long brandId, String brandName, List<Mention> mentions) {
        var byChannel = mentions.stream()
                .filter(m -> m.getSourcePlatform() != null)
                .collect(Collectors.groupingBy(Mention::getSourcePlatform));

        for (Map.Entry<String, List<Mention>> entry : byChannel.entrySet()) {
            var channelType = entry.getKey();
            var channelMentions = entry.getValue();

            String insightText = generateChannelInsightText(brandName, channelType, channelMentions);

            var existing = channelInsightRepository.findByBrandIdAndChannelType(brandId, channelType);
            var toSave = existing.isPresent()
                    ? existing.get().withText(insightText)
                    : ChannelInsight.create(brandId, channelType, insightText);
            channelInsightRepository.save(toSave);
        }
    }

    private String generateChannelInsightText(String brandName, String channelType, List<Mention> mentions) {
        try {
            String sample = mentions.stream()
                    .limit(10)
                    .map(Mention::getContent)
                    .collect(Collectors.joining("\n"));

            String prompt = String.format("""
                    Analiza estas menciones sobre la marca "%s" en %s. Genera UN insight breve
                    (máximo 1 oración, directo y accionable) sobre el patrón que ves.

                    Menciones:
                    %s

                    Responde solo con el insight, sin introducción ni formato.
                    """, brandName, channelType, sample);

            String result = groqApiClient.chat(prompt);
            if (result == null || result.isBlank()) {
                throw new IllegalStateException("Empty response from Groq");
            }
            return result.trim();
        } catch (Exception e) {
            log.warn("InsightGenerationService - Groq failed for channel {}, falling back to rule-based: {}",
                    channelType, e.getMessage());
            return buildRuleBasedInsight(mentions);
        }
    }

    public String generateCrisisAnalysis(String brandName, List<Mention> mentions, double negativePercent) {
        if (negativePercent < 30) return null;

        try {
            String negativeContent = mentions.stream()
                    .filter(m -> m.getSentimentCompound().doubleValue() < -0.3)
                    .limit(5)
                    .map(Mention::getContent)
                    .collect(Collectors.joining("\n"));

            String prompt = String.format("""
                    Analiza estas menciones negativas sobre la marca "%s" y genera un diagnóstico breve (máximo 2 oraciones) 
                    sobre el patrón de insatisfacción detectado. Sé específico y directo.

                    Menciones:
                    %s

                    Responde solo con el diagnóstico, sin introducción ni formato.
                    """, brandName, negativeContent);

            String result = groqApiClient.chat(prompt);
            if (result == null || result.isBlank()) {
                throw new IllegalStateException("Empty response from Groq");
            }
            return result.trim();
        } catch (Exception e) {
            log.warn("InsightGenerationService - Groq failed for crisis analysis, falling back to rule-based: {}",
                    e.getMessage());
            long negativeCount = mentions.stream()
                    .filter(m -> m.getSentimentCompound().doubleValue() < -0.3)
                    .count();
            return String.format(
                    "Se detectaron %d menciones negativas (%.0f%% del total) sobre %s. Revisión manual recomendada.",
                    negativeCount, negativePercent, brandName);
        }
    }

    private String buildRuleBasedInsight(List<Mention> mentions) {
        int total = mentions.size();
        if (total == 0) return "Sin datos suficientes";

        long negative = mentions.stream()
                .filter(m -> m.getSentimentCompound() != null && m.getSentimentCompound().doubleValue() < -0.3)
                .count();
        long positive = mentions.stream()
                .filter(m -> m.getSentimentCompound() != null && m.getSentimentCompound().doubleValue() > 0.3)
                .count();

        double negativeRatio = (double) negative / total * 100;
        double positiveRatio = (double) positive / total * 100;

        if (negativeRatio >= 50) {
            return String.format("%.0f%% de las menciones son negativas — requiere atención", negativeRatio);
        } else if (positiveRatio >= 50) {
            return String.format("%.0f%% de las menciones son positivas — canal saludable", positiveRatio);
        } else {
            return "Sentimiento mixto, sin tendencia clara";
        }
    }
}