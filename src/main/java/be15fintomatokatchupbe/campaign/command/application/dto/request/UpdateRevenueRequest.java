package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRevenueRequest extends BasePipelineRequest{
    private Long pipelineId;

    private List<InfluencerRevenueRequest> influencerList;

    private Long productPrice;

}
