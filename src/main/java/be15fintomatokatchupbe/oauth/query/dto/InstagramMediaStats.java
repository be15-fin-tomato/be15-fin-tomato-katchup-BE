package be15fintomatokatchupbe.oauth.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstagramMediaStats {
    private String mediaId;
    private String caption;
    private String mediaType; // IMAGE, VIDEO, REEL
    private int impressions;
    private int reach;
    private int engagement;
}

