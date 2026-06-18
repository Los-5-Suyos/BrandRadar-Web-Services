package brandradar.brandworkspace.domain.model.repositories;

import brandradar.brandworkspace.domain.model.aggregates.WorkspaceAccessAudit;

public interface WorkspaceAccessAuditRepository {
    WorkspaceAccessAudit save(WorkspaceAccessAudit audit);
}
