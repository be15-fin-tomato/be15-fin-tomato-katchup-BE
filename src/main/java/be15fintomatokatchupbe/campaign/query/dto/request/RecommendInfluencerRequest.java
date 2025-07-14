package be15fintomatokatchupbe.campaign.query.dto.request;

import be15fintomatokatchupbe.influencer.command.domain.aggregate.entity.Influencer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RecommendInfluencerRequest {
    /* 영향력 등급 */
    private Long influenceLevel;

    /* 알고리즘 스코어 */
    private Double algorithmScore;

    /* 카테고리 */
    private Boolean categories;

    /* 선호 성별 */
    private Long preferredGender;

    /* 선호 연령 */
    private Long preferredAgeRange;

    /* 광고주 만족도 */
    private Double advertiserSatisfaction;

    /* 광고 횟수 */
    private Long advertisementCount;

    /* 팔로워, 구독자 수 */
    private Long followerCount;
}