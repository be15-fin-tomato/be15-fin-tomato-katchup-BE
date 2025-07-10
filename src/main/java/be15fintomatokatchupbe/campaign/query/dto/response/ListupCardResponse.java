package be15fintomatokatchupbe.campaign.query.dto.response;

import be15fintomatokatchupbe.campaign.query.dto.response.common.CardResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ListupCardResponse extends CardResponse {
    private String productName;
    private List<String> influencerList;
}
