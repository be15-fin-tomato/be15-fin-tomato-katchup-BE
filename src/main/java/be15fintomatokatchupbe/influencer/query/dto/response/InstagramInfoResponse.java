package be15fintomatokatchupbe.influencer.query.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstagramInfoResponse {
    private String accountId;
    private String name;
    private Long follower;
    private String thumbnailUrl;
}
