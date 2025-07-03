package be15fintomatokatchupbe.oauth.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentInfo {
    private String text;
    private int likeCount;
}
