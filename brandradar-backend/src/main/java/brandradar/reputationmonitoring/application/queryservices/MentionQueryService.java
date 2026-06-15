package brandradar.reputationmonitoring.application.queryservices;

import brandradar.reputationmonitoring.application.queries.GetMentionsByBrandIdQuery;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;

import java.util.List;

public interface MentionQueryService {
    List<Mention> handle(GetMentionsByBrandIdQuery query);
}