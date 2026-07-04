package brandradar.reputationmonitoring.domain.model.aggregates;

import java.time.Instant;

public class ReputationReportSchedule {

    private final Long id;
    private final Long workspaceId;
    private final String email;
    private final String frequency;
    private final String dayOfWeek;
    private final String format;
    private final Boolean isActive;
    private final Instant nextRunAt;

    private ReputationReportSchedule(Long id, Long workspaceId, String email, String frequency,
                                     String dayOfWeek, String format, Boolean isActive, Instant nextRunAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.email = email;
        this.frequency = frequency;
        this.dayOfWeek = dayOfWeek;
        this.format = format;
        this.isActive = isActive != null ? isActive : true;
        this.nextRunAt = nextRunAt;
    }

    public static ReputationReportSchedule create(Long workspaceId, String email, String frequency,
                                                  String dayOfWeek, String format) {
        return new ReputationReportSchedule(null, workspaceId, email, frequency, dayOfWeek, format,
                true, computeNextRun(frequency, dayOfWeek));
    }

    public static ReputationReportSchedule rehydrate(Long id, Long workspaceId, String email, String frequency,
                                                     String dayOfWeek, String format, Boolean isActive,
                                                     Instant nextRunAt) {
        return new ReputationReportSchedule(id, workspaceId, email, frequency, dayOfWeek, format, isActive, nextRunAt);
    }

    public ReputationReportSchedule withUpdates(String email, String frequency, String dayOfWeek,
                                                String format, Boolean isActive) {
        String finalFrequency = frequency != null ? frequency : this.frequency;
        String finalDayOfWeek = dayOfWeek != null ? dayOfWeek : this.dayOfWeek;
        return new ReputationReportSchedule(this.id, this.workspaceId,
                email != null ? email : this.email,
                finalFrequency, finalDayOfWeek,
                format != null ? format : this.format,
                isActive != null ? isActive : this.isActive,
                computeNextRun(finalFrequency, finalDayOfWeek));
    }

    private static Instant computeNextRun(String frequency, String dayOfWeek) {
        // Cálculo simple: próxima ejecución en 7 días (semanal) o 30 días (mensual) desde ahora.
        // El día de la semana exacto se resuelve en el job real cuando se conecte un envío de email de verdad.
        long daysToAdd = "MONTHLY".equalsIgnoreCase(frequency) ? 30 : 7;
        return Instant.now().plus(daysToAdd, java.time.temporal.ChronoUnit.DAYS);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getEmail() { return email; }
    public String getFrequency() { return frequency; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getFormat() { return format; }
    public Boolean getIsActive() { return isActive; }
    public Instant getNextRunAt() { return nextRunAt; }
}