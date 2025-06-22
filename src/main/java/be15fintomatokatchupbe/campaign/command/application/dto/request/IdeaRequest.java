package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class IdeaRequest {
    private String content;
}
