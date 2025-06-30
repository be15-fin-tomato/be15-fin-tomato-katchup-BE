package be15fintomatokatchupbe.oauth.query.dto.request;

import lombok.Getter;

@Getter
public class InstagramPermalinkRequest {
    private String accessToken;
    private String igUserId;
    private String permalink;
}
