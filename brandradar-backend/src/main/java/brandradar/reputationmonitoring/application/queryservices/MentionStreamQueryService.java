package brandradar.reputationmonitoring.application.queryservices;

import brandradar.reputationmonitoring.application.queries.GetMentionStreamsByBrandIdQuery;
import brandradar.reputationmonitoring.domain.model.aggregates.MentionStream;

import java.util.List;

public interface MentionStreamQueryService {
    List<MentionStream> handle(GetMentionStreamsByBrandIdQuery query);
}