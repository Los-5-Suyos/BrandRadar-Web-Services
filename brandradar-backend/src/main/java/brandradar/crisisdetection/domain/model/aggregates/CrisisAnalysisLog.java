package brandradar.crisisdetection.domain.model.aggregates;

import java.time.Instant;

public class CrisisAnalysisLog {

    private final Long id;
    private final Long incidentId;
    private final String pattern;
    private final String keywords;
    private final String geofocus;
    private final String diagnostico;
    private final String accion;
    private final Instant createdAt;

    private CrisisAnalysisLog(Long id, Long incidentId, String pattern, String keywords,
                              String geofocus, String diagnostico, String accion, Instant createdAt) {
        this.id = id;
        this.incidentId = incidentId;
        this.pattern = pattern;
        this.keywords = keywords;
        this.geofocus = geofocus;
        this.diagnostico = diagnostico;
        this.accion = accion;
        this.createdAt = createdAt;
    }

    public static CrisisAnalysisLog create(Long incidentId, String pattern, String keywords,
                                           String geofocus, String diagnostico, String accion) {
        return new CrisisAnalysisLog(null, incidentId, pattern, keywords, geofocus, diagnostico, accion, null);
    }

    public static CrisisAnalysisLog rehydrate(Long id, Long incidentId, String pattern, String keywords,
                                              String geofocus, String diagnostico, String accion, Instant createdAt) {
        return new CrisisAnalysisLog(id, incidentId, pattern, keywords, geofocus, diagnostico, accion, createdAt);
    }

    public Long getId() { return id; }
    public Long getIncidentId() { return incidentId; }
    public String getPattern() { return pattern; }
    public String getKeywords() { return keywords; }
    public String getGeofocus() { return geofocus; }
    public String getDiagnostico() { return diagnostico; }
    public String getAccion() { return accion; }
    public Instant getCreatedAt() { return createdAt; }
}