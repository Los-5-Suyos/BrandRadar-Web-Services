package brandradar.crisisdetection.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record UpdateAlertPreferenceResource(@NotNull Boolean enabled) {}