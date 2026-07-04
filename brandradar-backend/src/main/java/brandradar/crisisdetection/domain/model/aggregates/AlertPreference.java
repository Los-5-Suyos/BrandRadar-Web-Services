package brandradar.crisisdetection.domain.model.aggregates;

public class AlertPreference {

    private final Long id;
    private final Long brandId;
    private final String key;
    private final Boolean enabled;

    private AlertPreference(Long id, Long brandId, String key, Boolean enabled) {
        this.id = id;
        this.brandId = brandId;
        this.key = key;
        this.enabled = enabled;
    }

    public static AlertPreference create(Long brandId, String key, Boolean enabled) {
        return new AlertPreference(null, brandId, key, enabled != null ? enabled : true);
    }

    public static AlertPreference rehydrate(Long id, Long brandId, String key, Boolean enabled) {
        return new AlertPreference(id, brandId, key, enabled);
    }

    public AlertPreference withEnabled(Boolean enabled) {
        return new AlertPreference(this.id, this.brandId, this.key, enabled);
    }

    public Long getId() { return id; }
    public Long getBrandId() { return brandId; }
    public String getKey() { return key; }
    public Boolean getEnabled() { return enabled; }
}