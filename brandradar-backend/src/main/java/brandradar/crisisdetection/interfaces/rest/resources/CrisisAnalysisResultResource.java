package brandradar.crisisdetection.interfaces.rest.resources;

public record CrisisAnalysisResultResource(
        String pattern,
        String keywords,
        String geofocus,
        String diagnostico,
        String accion
) {}