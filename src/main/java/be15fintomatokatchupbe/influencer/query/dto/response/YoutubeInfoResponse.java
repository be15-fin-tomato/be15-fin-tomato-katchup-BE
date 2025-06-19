package be15fintomatokatchupbe.influencer.query.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YoutubeInfoResponse {
    private String accountId;
    private String name;
    private Long subscriber;
    private String thumbnailUrl;
}
