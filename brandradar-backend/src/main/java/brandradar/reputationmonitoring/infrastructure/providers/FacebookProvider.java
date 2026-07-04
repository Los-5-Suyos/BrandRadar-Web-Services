package brandradar.reputationmonitoring.infrastructure.providers;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.services.ChannelMentionProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Pendiente de integración real — requiere Meta Graph API con permisos de página,
 * o créditos adicionales de SociaVault. No implementado en esta entrega.
 */
@Component
public class FacebookProvider implements ChannelMentionProvider {

    @Override
    public String getChannelType() {
        return "FACEBOOK";
    }

    @Override
    public List<Mention> fetchMentions(Long brandId, String brandName) {
        return List.of();
    }
}