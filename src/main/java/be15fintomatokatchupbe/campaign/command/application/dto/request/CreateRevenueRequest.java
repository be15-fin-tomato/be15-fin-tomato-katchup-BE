package be15fintomatokatchupbe.campaign.command.application.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateRevenueRequest extends BasePipelineRequest{
    // 인플루언서
    private List<InfluencerRevenueRequest> influencerList;
    private List<IdeaRequest> ideaList;

    // 광고 단가, 상품 가격
    private Long productPrice;
    private Long salesQuantity;

}
