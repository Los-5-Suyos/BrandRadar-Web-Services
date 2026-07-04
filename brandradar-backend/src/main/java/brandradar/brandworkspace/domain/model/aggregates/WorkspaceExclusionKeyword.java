package brandradar.brandworkspace.domain.model.aggregates;

public class WorkspaceExclusionKeyword {

    private final Long id;
    private final Long workspaceId;
    private final String keyword;

    private WorkspaceExclusionKeyword(Long id, Long workspaceId, String keyword) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.keyword = keyword;
    }

    public static WorkspaceExclusionKeyword create(Long workspaceId, String keyword) {
        return new WorkspaceExclusionKeyword(null, workspaceId, keyword);
    }

    public static WorkspaceExclusionKeyword rehydrate(Long id, Long workspaceId, String keyword) {
        return new WorkspaceExclusionKeyword(id, workspaceId, keyword);
    }

    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getKeyword() { return keyword; }
}