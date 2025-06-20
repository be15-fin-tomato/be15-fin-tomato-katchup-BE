package be15fintomatokatchupbe.influencer.command.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InfluencerRegisterRequestDTO {
    private String name;
    private String gender;
    private Long price;
    private String national;
    private Long userId;

    private boolean youtubeConnected;
    private String youtubeAccountId;

    private boolean instagramConnected;
    private String instagramAccountId;
    private Long instagramFollower;

    private List<Long> categoryIds;
}
