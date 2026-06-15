package brandradar.reputationmonitoring.application.internal.commandservices;

import brandradar.reputationmonitoring.application.commands.CreateMentionCommand;
import brandradar.reputationmonitoring.application.commandservices.MentionCommandService;
import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import brandradar.reputationmonitoring.domain.model.repositories.MentionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class MentionCommandServiceImpl implements MentionCommandService {

    private final MentionRepository mentionRepository;

    public MentionCommandServiceImpl(MentionRepository mentionRepository) {
        this.mentionRepository = mentionRepository;
    }

    @Override
    @Transactional
    public Optional<Mention> handle(CreateMentionCommand command) {
        var mention = Mention.create(command.brandId(), command.content(), command.sourcePlatform(),
                command.sourceUrl(), command.author(), command.publishedAt());
        var saved = mentionRepository.save(mention);
        log.info("Mention created with id={}", saved.getId());
        return Optional.of(saved);
    }
}