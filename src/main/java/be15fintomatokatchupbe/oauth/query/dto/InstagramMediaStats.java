package be15fintomatokatchupbe.oauth.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstagramMediaStats {
    private String mediaId;
    private String mediaType;
    private String mediaUrl;

    private Integer impressions;  // 사용 안 해도 일단 유지
    private Integer reach;        // 도달 수

    private Integer viewCount;    // 조회수
    private Integer likeCount;    // 좋아요
    private Integer commentCount; // 댓글
    private Integer saveCount;    // 저장
    private Integer shareCount;   // 공유

    private Integer engagement;
}

