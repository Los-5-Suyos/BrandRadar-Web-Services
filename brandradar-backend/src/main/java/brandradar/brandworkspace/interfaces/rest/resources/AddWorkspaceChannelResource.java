package brandradar.brandworkspace.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddWorkspaceChannelResource(
        @NotBlank @Pattern(regexp = "TWITTER|INSTAGRAM|FACEBOOK|NEWS|REDDIT|TIKTOK") String channelType
) {}