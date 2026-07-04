package brandradar.reputationmonitoring.interfaces.rest.transform;

import brandradar.reputationmonitoring.application.commands.CreateMentionCommand;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.interfaces.rest.resources.CreateMentionResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.MentionResource;

public class MentionAssembler {

    private MentionAssembler() {}

    public static CreateMentionCommand toCommand(CreateMentionResource resource) {
        return new CreateMentionCommand(
                resource.brandId(),
                resource.content(),
                resource.sourcePlatform(),
                resource.sourceUrl(),
                resource.author(),
                resource.publishedAt()
        );
    }

    public static MentionResource toResource(Mention mention) {
        return new MentionResource(
                mention.getId(),
                mention.getMentionStreamId(),
                mention.getBrandId(),
                mention.getContent(),
                mention.getSourcePlatform(),
                mention.getSourceUrl(),
                mention.getSourceReliability(),
                mention.getAuthor(),
                mention.getAuthorHandle(),
                mention.getPublishedAt(),
                mention.getCategory(),
                mention.getSentimentPositive(),
                mention.getSentimentNegative(),
                mention.getSentimentNeutral(),
                mention.getSentimentCompound(),
                mention.getSentimentConfidence(),
                mention.getEngagementLikes(),
                mention.getEngagementComments(),
                mention.getEngagementViews(),
                mention.getStatus(),
                mention.getCreatedAt()
        );
    }
}