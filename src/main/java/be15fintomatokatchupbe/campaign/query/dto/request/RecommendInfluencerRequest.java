package be15fintomatokatchupbe.campaign.query.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendInfluencerRequest {
    /* 파이프라인 Id */
    private Long campaignId;

    /* 활성도 등급 */
    private Long engagement ;

    /* 알고리즘 스코어 */
    private Double algorithmScore;

    /* 카테고리 */
    private Boolean categories;

    /* 선호 성별 */
    /* 1 남자 2 여자 */
    private Long preferredGender;

    /* 선호 연령 */
    private Long preferredAgeRange;

    /* 광고주 만족도 */
    private Double advertiserSatisfaction;

    /* 팔로워, 구독자 수 */
    private Long followerCount;
    @Override
    public String toString() {
        return "RecommendInfluencerRequest{" +
                "campaignId=" + campaignId +
                ", engagement =" + engagement  +
                ", algorithmScore=" + algorithmScore +
                ", categories=" + categories +
                ", preferredGender=" + preferredGender +
                ", preferredAgeRange=" + preferredAgeRange +
                ", advertiserSatisfaction=" + advertiserSatisfaction +
                ", followerCount=" + followerCount +
                '}';
    }
}