package brandradar.reputationmonitoring.application.services;

import brandradar.brandworkspace.domain.model.repositories.BrandRepository;
import brandradar.brandworkspace.domain.model.repositories.WorkspaceChannelRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.KeywordRuleJpaEntity;
import brandradar.brandworkspace.infrastructure.persistence.jpa.entities.WorkspaceExclusionKeywordJpaEntity;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataKeywordRuleRepository;
import brandradar.brandworkspace.infrastructure.persistence.jpa.repositories.SpringDataWorkspaceExclusionKeywordRepository;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.reputationmonitoring.domain.model.services.ChannelMentionProvider;
import brandradar.sentimentintelligence.application.services.MentionSentimentAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MentionIngestionService {

    private final List<ChannelMentionProvider> providers;
    private final BrandRepository brandRepository;
    private final WorkspaceChannelRepository workspaceChannelRepository;
    private final MentionRepository mentionRepository;
    private final SpringDataKeywordRuleRepository keywordRuleRepository;
    private final SpringDataWorkspaceExclusionKeywordRepository exclusionKeywordRepository;
    private final MentionSentimentAnalyzer sentimentAnalyzer;

    public MentionIngestionService(List<ChannelMentionProvider> providers,
                                   BrandRepository brandRepository,
                                   WorkspaceChannelRepository workspaceChannelRepository,
                                   MentionRepository mentionRepository,
                                   SpringDataKeywordRuleRepository keywordRuleRepository,
                                   SpringDataWorkspaceExclusionKeywordRepository exclusionKeywordRepository,
                                   MentionSentimentAnalyzer sentimentAnalyzer) {
        this.providers = providers;
        this.brandRepository = brandRepository;
        this.workspaceChannelRepository = workspaceChannelRepository;
        this.mentionRepository = mentionRepository;
        this.keywordRuleRepository = keywordRuleRepository;
        this.exclusionKeywordRepository = exclusionKeywordRepository;
        this.sentimentAnalyzer = sentimentAnalyzer;
    }

    @Transactional
    public List<Mention> ingestForBrand(Long brandId, String brandName) {
        log.info("MentionIngestionService - Starting ingestion for brand id={} name={}", brandId, brandName);

        var brandOpt = brandRepository.findById(brandId);
        if (brandOpt.isEmpty()) {
            log.warn("MentionIngestionService - Brand {} not found, skipping ingestion", brandId);
            return List.of();
        }
        var workspaceId = brandOpt.get().getWorkspaceId();

        Set<String> activeChannelTypes = workspaceChannelRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(channel -> channel.getChannelType())
                .collect(Collectors.toSet());

        log.info("MentionIngestionService - Active channels for workspace {}: {}", workspaceId, activeChannelTypes);

        Set<String> inclusionKeywords = keywordRuleRepository.findByBrandIdAndIsActiveTrue(brandId)
                .stream()
                .map(KeywordRuleJpaEntity::getKeyword)
                .map(k -> k.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        Set<String> exclusionKeywords = exclusionKeywordRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(WorkspaceExclusionKeywordJpaEntity::getKeyword)
                .map(k -> k.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        log.info("MentionIngestionService - Inclusion keywords: {} | Exclusion keywords: {}",
                inclusionKeywords, exclusionKeywords);

        List<Mention> allMentions = new ArrayList<>();
        for (ChannelMentionProvider provider : providers) {
            if (!activeChannelTypes.contains(provider.getChannelType())) {
                continue;
            }
            try {
                var mentions = provider.fetchMentions(brandId, brandName);
                log.info("MentionIngestionService - {} returned {} mentions (antes de filtrar keywords)",
                        provider.getChannelType(), mentions.size());
                allMentions.addAll(mentions);
            } catch (Exception e) {
                log.error("MentionIngestionService - Provider {} failed: {}", provider.getChannelType(), e.getMessage());
            }
        }

        log.info("MentionIngestionService - Total raw mentions fetched: {}", allMentions.size());

        List<Mention> filtered = allMentions.stream()
                .filter(m -> matchesInclusionKeywords(m, inclusionKeywords))
                .filter(m -> !matchesExclusionKeywords(m, exclusionKeywords))
                .toList();

        int discardedByKeywords = allMentions.size() - filtered.size();
        if (discardedByKeywords > 0) {
            log.info("MentionIngestionService - {} menciones descartadas por keywords de inclusión/exclusión",
                    discardedByKeywords);
        }

        // Deduplicar ANTES de analizar sentimiento — así no gastamos Groq en menciones que
        // de todas formas se van a descartar por ya existir.
        List<Mention> newMentions = filtered.stream()
                .filter(mention -> mention.getSourceUrl() == null || !mentionRepository.existsBySourceUrl(mention.getSourceUrl()))
                .toList();

        log.info("MentionIngestionService - {} menciones nuevas (no duplicadas) a analizar", newMentions.size());

        List<Mention> analyzed = newMentions.isEmpty()
                ? newMentions
                : sentimentAnalyzer.analyzeAll(brandName, newMentions);

        List<Mention> saved = analyzed.stream()
                .map(mentionRepository::save)
                .toList();

        log.info("MentionIngestionService - Saved {} mentions for brand id={}", saved.size(), brandId);
        return saved;
    }

    private boolean matchesInclusionKeywords(Mention mention, Set<String> inclusionKeywords) {
        if ("YOUTUBE".equals(mention.getSourcePlatform())) return true;
        if (inclusionKeywords.isEmpty()) return true;
        String content = mention.getContent() != null ? mention.getContent().toLowerCase(Locale.ROOT) : "";
        return inclusionKeywords.stream().anyMatch(content::contains);
    }

    private boolean matchesExclusionKeywords(Mention mention, Set<String> exclusionKeywords) {
        if (exclusionKeywords.isEmpty()) return false;
        String content = mention.getContent() != null ? mention.getContent().toLowerCase(Locale.ROOT) : "";
        return exclusionKeywords.stream().anyMatch(content::contains);
    }
}