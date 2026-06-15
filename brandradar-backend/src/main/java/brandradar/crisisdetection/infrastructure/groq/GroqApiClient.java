package brandradar.crisisdetection.infrastructure.groq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class GroqApiClient {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.model}")
    private String model;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String chat(String userMessage) {
        try {
            var escapedMessage = userMessage
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");

            var requestBody = String.format("""
                {
                    "model": "%s",
                    "messages": [
                        {
                            "role": "system",
                            "content": "Eres un experto en gestión de reputación de marcas. Analiza crisis de reputación y responde SIEMPRE en JSON válido con estos campos: pattern, keywords, geofocus, diagnostico, accion."
                        },
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ],
                    "temperature": 0.3,
                    "max_tokens": 1000
                }
                """, model, escapedMessage);

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Groq API response status: {}", response.statusCode());
            log.info("Groq API response body: {}", response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Groq API error: " + response.body());
            }

            var responseBody = objectMapper.readTree(response.body());
            return responseBody
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        } catch (Exception e) {
            log.error("Error calling Groq API: {}", e.getMessage());
            throw new RuntimeException("Error calling Groq API", e);
        }
    }
}