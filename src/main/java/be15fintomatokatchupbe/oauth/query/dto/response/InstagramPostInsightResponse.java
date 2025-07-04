package be15fintomatokatchupbe.oauth.query.dto.response;

import be15fintomatokatchupbe.oauth.query.dto.CommentInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
    private List<CommentInfo> commentContent;
}
