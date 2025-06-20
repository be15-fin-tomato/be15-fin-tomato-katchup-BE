package be15fintomatokatchupbe.influencer.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfluencerEditRequestDTO {

    private Long influencerId;

    private String name;
    private String gender;
    private Long price;
    private String national;
    private String userName;

    private List<Long> categoryIds;
}
