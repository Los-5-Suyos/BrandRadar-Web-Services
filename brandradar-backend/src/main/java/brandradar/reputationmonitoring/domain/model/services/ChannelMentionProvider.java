package brandradar.reputationmonitoring.domain.model.services;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;

import java.util.List;

public interface ChannelMentionProvider {

    String getChannelType();

    List<Mention> fetchMentions(Long brandId, String brandName);
}