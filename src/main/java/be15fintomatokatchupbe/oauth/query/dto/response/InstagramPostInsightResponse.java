package be15fintomatokatchupbe.oauth.query.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstagramPostInsightResponse {
    private String permalink;
    private int views;
    private int reach;
    private int likes;
    private int comments;
    private int shares;
    private int saved;
}
