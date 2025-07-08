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
    private boolean instagramConnected;

     /* 유튜브 , 인스타그램 연동 분리 */
//    private String youtubeAccountId;
//    private String instagramAccountId;
//    private Long instagramFollower;

    private List<Long> categoryIds;
}
