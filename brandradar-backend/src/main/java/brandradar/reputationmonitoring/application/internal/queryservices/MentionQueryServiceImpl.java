package brandradar.reputationmonitoring.application.internal.queryservices;

import brandradar.reputationmonitoring.application.queries.GetMentionsByBrandIdQuery;
import brandradar.reputationmonitoring.application.queryservices.MentionQueryService;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentionQueryServiceImpl implements MentionQueryService {

    private final MentionRepository mentionRepository;

    public MentionQueryServiceImpl(MentionRepository mentionRepository) {
        this.mentionRepository = mentionRepository;
    }

    @Override
    public List<Mention> handle(GetMentionsByBrandIdQuery query) {
        return mentionRepository.findByBrandId(query.brandId());
    }
}