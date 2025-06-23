package be15fintomatokatchupbe.oauth.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class YoutubeCodeResponse {
    private String channelId;
}
