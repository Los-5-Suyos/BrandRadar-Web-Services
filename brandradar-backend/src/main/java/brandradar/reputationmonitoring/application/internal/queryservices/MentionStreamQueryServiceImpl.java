package brandradar.reputationmonitoring.application.internal.queryservices;

import brandradar.reputationmonitoring.application.queries.GetMentionStreamsByBrandIdQuery;
import brandradar.reputationmonitoring.application.queryservices.MentionStreamQueryService;
import brandradar.reputationmonitoring.domain.model.aggregates.MentionStream;
import brandradar.reputationmonitoring.domain.model.repositories.MentionStreamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentionStreamQueryServiceImpl implements MentionStreamQueryService {

    private final MentionStreamRepository mentionStreamRepository;

    public MentionStreamQueryServiceImpl(MentionStreamRepository mentionStreamRepository) {
        this.mentionStreamRepository = mentionStreamRepository;
    }

    @Override
    public List<MentionStream> handle(GetMentionStreamsByBrandIdQuery query) {
        return mentionStreamRepository.findByBrandId(query.brandId());
    }
}