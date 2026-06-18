package brandradar.reputationmonitoring.application.services;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import brandradar.reputationmonitoring.infrastructure.providers.MockMentionProvider;
import brandradar.reputationmonitoring.infrastructure.providers.YouTubeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MentionIngestionService {

    private final MockMentionProvider mockMentionProvider;
    private final YouTubeProvider youTubeProvider;
    private final MentionRepository mentionRepository;

    public MentionIngestionService(MockMentionProvider mockMentionProvider, YouTubeProvider youTubeProvider, MentionRepository mentionRepository) {
        this.mockMentionProvider = mockMentionProvider;
        this.youTubeProvider = youTubeProvider;
        this.mentionRepository = mentionRepository;
    }

    @Transactional
    public List<Mention> ingestForBrand(Long brandId, String brandName) {
        log.info("MentionIngestionService - Starting ingestion for brand id={} name={}", brandId, brandName);

        List<Mention> allMentions = new ArrayList<>();

        allMentions.addAll(mockMentionProvider.generateMentions(brandId, brandName, 15));
        allMentions.addAll(youTubeProvider.fetchMentions(brandId, brandName));

        log.info("MentionIngestionService - Total raw mentions fetched: {}", allMentions.size());

        List<Mention> saved = allMentions.stream()
                .filter(mention -> !mentionRepository.existsBySourceUrl(mention.getSourceUrl()))
                .map(mentionRepository::save)
                .toList();
        
        log.info("MentionIngestionService - Saved {} mentions for brand id={}", saved.size(), brandId);
        return saved;
    }
}