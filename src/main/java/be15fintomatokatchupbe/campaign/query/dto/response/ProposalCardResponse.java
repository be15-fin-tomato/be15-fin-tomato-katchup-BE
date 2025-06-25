package be15fintomatokatchupbe.campaign.query.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ProposalCardResponse extends CardResponse{
    private String  requestAt;   // 요청일
    private String presentAt;   // 발표일
}
