package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.services.ChannelMentionProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Pendiente de integración real — requiere créditos adicionales de SociaVault
 * o Meta Graph API. No implementado en esta entrega.
 */
@Component
public class InstagramProvider implements ChannelMentionProvider {

    @Override
    public String getChannelType() {
        return "INSTAGRAM";
    }

    @Override
    public List<Mention> fetchMentions(Long brandId, String brandName) {
        return List.of();
    }
}