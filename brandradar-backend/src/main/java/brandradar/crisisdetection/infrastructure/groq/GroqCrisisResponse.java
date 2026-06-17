package brandradar.crisisdetection.infrastructure.groq;

public record GroqCrisisResponse(
        String pattern,
        String keywords,
        String geofocus,
        String diagnostico,
        String accion
) {}