package brandradar.reputationmonitoring.interfaces.rest.transform;

import brandradar.reputationmonitoring.application.commands.CreateMentionStreamCommand;
import brandradar.reputationmonitoring.domain.model.aggregates.MentionStream;
import brandradar.reputationmonitoring.interfaces.rest.resources.CreateMentionStreamResource;
import brandradar.reputationmonitoring.interfaces.rest.resources.MentionStreamResource;

public class MentionStreamAssembler {

    private MentionStreamAssembler() {}

    public static CreateMentionStreamCommand toCommand(CreateMentionStreamResource resource) {
        return new CreateMentionStreamCommand(
                resource.brandId(),
                resource.periodFrom(),
                resource.periodTo()
        );
    }

    public static MentionStreamResource toResource(MentionStream mentionStream) {
        return new MentionStreamResource(
                mentionStream.getId(),
                mentionStream.getBrandId(),
                mentionStream.getPeriodFrom(),
                mentionStream.getPeriodTo(),
                mentionStream.getStatus(),
                mentionStream.getCreatedAt(),
                mentionStream.getUpdatedAt()
        );
    }
}