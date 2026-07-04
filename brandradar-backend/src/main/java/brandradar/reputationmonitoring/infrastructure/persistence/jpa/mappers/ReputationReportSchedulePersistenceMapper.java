package brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationReportSchedule;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationReportScheduleJpaEntity;

public class ReputationReportSchedulePersistenceMapper {

    private ReputationReportSchedulePersistenceMapper() {}

    public static ReputationReportScheduleJpaEntity toJpaEntity(ReputationReportSchedule schedule) {
        return new ReputationReportScheduleJpaEntity(
                schedule.getId(),
                schedule.getWorkspaceId(),
                schedule.getEmail(),
                schedule.getFrequency(),
                schedule.getDayOfWeek(),
                schedule.getFormat(),
                schedule.getIsActive(),
                schedule.getNextRunAt()
        );
    }

    public static ReputationReportSchedule toDomain(ReputationReportScheduleJpaEntity entity) {
        return ReputationReportSchedule.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                entity.getEmail(),
                entity.getFrequency(),
                entity.getDayOfWeek(),
                entity.getFormat(),
                entity.getIsActive(),
                entity.getNextRunAt()
        );
    }
}