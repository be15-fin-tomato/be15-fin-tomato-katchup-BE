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
    private String mediaId;       // 인스타그램 미디어 ID
    private String mediaType;     // IMAGE, VIDEO, REELS 등
    private String mediaUrl;      // 미디어 썸네일 또는 콘텐츠 URL
    private Integer impressions;  // 노출 수
    private Integer reach;        // 도달 수
    private Integer engagement;   // 참여 수 (좋아요 + 댓글 등)
}

