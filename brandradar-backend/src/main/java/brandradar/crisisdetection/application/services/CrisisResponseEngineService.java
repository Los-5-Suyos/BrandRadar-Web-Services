package brandradar.crisisdetection.application.services;

import brandradar.crisisdetection.infrastructure.groq.GroqApiClient;
import brandradar.crisisdetection.infrastructure.groq.GroqCrisisResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CrisisResponseEngineService {

    private final GroqApiClient groqApiClient;
    private final ObjectMapper objectMapper;

    public CrisisResponseEngineService(GroqApiClient groqApiClient) {
        this.groqApiClient = groqApiClient;
        this.objectMapper = new ObjectMapper();
    }

    public GroqCrisisResponse analyzeCrisis(String brandName, String crisisDescription) {
        var prompt = """
            Analiza esta crisis de reputación para la marca "%s":
            
            Descripción: %s
            
            Responde SOLO con un JSON válido sin texto adicional, sin markdown, sin explicaciones:
            {
                "pattern": "tipo de patrón detectado",
                "keywords": "palabras clave separadas por comas",
                "geofocus": "región o área geográfica afectada",
                "diagnostico": "diagnóstico detallado de la crisis",
                "accion": "acción recomendada inmediata"
            }
            """.formatted(brandName, crisisDescription);

        try {
            var rawResponse = groqApiClient.chat(prompt);
            log.info("Groq raw response: {}", rawResponse);

            // Buscar el JSON dentro de la respuesta
            int start = rawResponse.indexOf("{");
            int end = rawResponse.lastIndexOf("}");

            if (start == -1 || end == -1) {
                log.error("No JSON found in response: {}", rawResponse);
                throw new RuntimeException("No JSON in response");
            }

            var cleanJson = rawResponse.substring(start, end + 1);
            log.info("Clean JSON: {}", cleanJson);

            return objectMapper.readValue(cleanJson, GroqCrisisResponse.class);
        } catch (Exception e) {
            log.error("Error parsing Groq response: {}", e.getMessage());
            return new GroqCrisisResponse(
                    "UNKNOWN",
                    "sin keywords",
                    "global",
                    "No se pudo analizar la crisis automáticamente",
                    "Revisar manualmente"
            );
        }
    }
}