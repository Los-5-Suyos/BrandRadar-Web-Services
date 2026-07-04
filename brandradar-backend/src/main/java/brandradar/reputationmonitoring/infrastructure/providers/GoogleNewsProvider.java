package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.services.ChannelMentionProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Pendiente de integración real — requiere una API de noticias (ej. NewsAPI,
 * Google Custom Search). No implementado en esta entrega.
 */
@Component
public class GoogleNewsProvider implements ChannelMentionProvider {

    @Override
    public String getChannelType() {
        return "GOOGLE_NEWS";
    }

    @Override
    public List<Mention> fetchMentions(Long brandId, String brandName) {
        return List.of();
    }
}