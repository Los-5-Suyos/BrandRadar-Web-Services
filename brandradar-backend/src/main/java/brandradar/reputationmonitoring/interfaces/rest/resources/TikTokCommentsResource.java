package brandradar.reputationmonitoring.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public record TikTokCommentsResource(
        Integer total,
        List<CommentItem> comments
) {
    public record CommentItem(
            String text,
            String authorName,
            String authorHandle,
            Integer likes,
            Instant publishedAt
    ) {}
}