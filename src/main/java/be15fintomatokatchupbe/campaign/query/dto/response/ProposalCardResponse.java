package be15fintomatokatchupbe.campaign.query.dto.response;


import be15fintomatokatchupbe.campaign.query.dto.response.common.CardResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ProposalCardResponse extends CardResponse {
    private String  requestAt;   // 요청일
    private String presentAt;   // 발표일
}
