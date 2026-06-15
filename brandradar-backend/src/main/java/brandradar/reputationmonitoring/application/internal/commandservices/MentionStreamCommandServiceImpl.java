package brandradar.reputationmonitoring.application.internal.commandservices;

import brandradar.reputationmonitoring.application.commands.CreateMentionStreamCommand;
import brandradar.reputationmonitoring.application.commandservices.MentionStreamCommandService;
import brandradar.reputationmonitoring.domain.model.aggregates.MentionStream;
import brandradar.reputationmonitoring.domain.model.repositories.MentionStreamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class MentionStreamCommandServiceImpl implements MentionStreamCommandService {

    private final MentionStreamRepository mentionStreamRepository;

    public MentionStreamCommandServiceImpl(MentionStreamRepository mentionStreamRepository) {
        this.mentionStreamRepository = mentionStreamRepository;
    }

    @Override
    @Transactional
    public Optional<MentionStream> handle(CreateMentionStreamCommand command) {
        var mentionStream = MentionStream.create(command.brandId(), command.periodFrom(), command.periodTo());
        var saved = mentionStreamRepository.save(mentionStream);
        log.info("MentionStream created with id={}", saved.getId());
        return Optional.of(saved);
    }
}