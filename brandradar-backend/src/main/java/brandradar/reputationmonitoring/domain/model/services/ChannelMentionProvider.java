package brandradar.reputationmonitoring.domain.model.services;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;

import java.util.List;

/**
 * Contrato común para cualquier fuente de menciones (YouTube, Twitter, Reddit, y a futuro
 * Facebook/Instagram/TikTok/Google News/Blogs). MentionIngestionService itera sobre todas
 * las implementaciones registradas en Spring y llama solo a las que correspondan a los
 * canales activos del workspace — así agregar una red nueva en el futuro es literal
 * escribir una clase nueva, sin tocar el orquestador.
 */
public interface ChannelMentionProvider {

    /**
     * @return el nombre del canal que esta implementación cubre, debe coincidir
     *         con los valores del ENUM de WorkspaceAnalyticsChannel (ej. "YOUTUBE", "TWITTER").
     */
    String getChannelType();

    /**
     * Trae menciones reales (o vacío si el canal aún no está conectado a una fuente real).
     */
    List<Mention> fetchMentions(Long brandId, String brandName);
}