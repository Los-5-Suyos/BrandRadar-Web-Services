package brandradar.reputationmonitoring.infrastructure.persistence.jpa.mappers;

import brandradar.reputationmonitoring.domain.model.aggregates.ReputationReport;
import brandradar.reputationmonitoring.infrastructure.persistence.jpa.entities.ReputationReportJpaEntity;

public class ReputationReportPersistenceMapper {

    private ReputationReportPersistenceMapper() {}

    public static ReputationReportJpaEntity toJpaEntity(ReputationReport report) {
        return new ReputationReportJpaEntity(
                report.getId(),
                report.getWorkspaceId(),
                report.getBrandId(),
                report.getTitle(),
                report.getPeriodFrom(),
                report.getPeriodTo(),
                report.getStatus(),
                report.getFormat(),
                report.getFileUrl(),
                report.getFileSizeBytes(),
                report.getRecipientsCount(),
                report.getGeneratedBy(),
                report.getGeneratedAt()
        );
    }

    public static ReputationReport toDomain(ReputationReportJpaEntity entity) {
        return ReputationReport.rehydrate(
                entity.getId(),
                entity.getWorkspaceId(),
                entity.getBrandId(),
                entity.getTitle(),
                entity.getPeriodFrom(),
                entity.getPeriodTo(),
                entity.getStatus(),
                entity.getFormat(),
                entity.getFileUrl(),
                entity.getFileSizeBytes(),
                entity.getRecipientsCount(),
                entity.getGeneratedBy(),
                entity.getGeneratedAt(),
                entity.getCreatedAt()
        );
    }
}