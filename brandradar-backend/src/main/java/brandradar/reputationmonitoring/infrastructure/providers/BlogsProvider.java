package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.services.ChannelMentionProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Pendiente de integración real — requiere scraping o API de blogs/RSS.
 * No implementado en esta entrega.
 */
@Component
public class BlogsProvider implements ChannelMentionProvider {

    @Override
    public String getChannelType() {
        return "BLOGS";
    }

    @Override
    public List<Mention> fetchMentions(Long brandId, String brandName) {
        return List.of();
    }
}