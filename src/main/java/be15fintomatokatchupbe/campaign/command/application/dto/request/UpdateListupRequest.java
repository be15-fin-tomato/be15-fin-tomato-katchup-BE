package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UpdateListupRequest {
    public Long pipelineId;
    public Long campaignId;
    public String name;
    public List<Long> influencerId;
}
