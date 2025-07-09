package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.influencer.command.application.support.YoutubeHelperService;
import be15fintomatokatchupbe.oauth.command.application.Service.YoutubeCommandService;
import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YoutubeAnalyticsQueryService {

    private final YoutubeOAuthQueryService youtubeApi;
    private final YoutubeHelperService youtubeHelper;

    public YoutubeStatsResponse getYoutubeStatsByInfluencer(Long influencerId, String startDate, String endDate) {
        // 유튜브 테이블에서 채널 ID 조회
        String channelId = youtubeHelper.findByInfluencerId(influencerId).getChannelId();

        // accessToken 조회 (Redis → 없으면 refresh)
        String accessToken = youtubeApi.getValidAccessToken(channelId);

        // 기본 수치 조회
        int totalVideos = youtubeApi.getTotalVideoCount(accessToken, channelId);
        long totalViews = youtubeApi.getTotalViews(accessToken, channelId, startDate, endDate);
        long totalLikes = youtubeApi.getTotalLikes(accessToken, channelId, startDate, endDate);
        long totalComments = youtubeApi.getTotalComments(accessToken, channelId, startDate, endDate);
        int dateRange = youtubeApi.getDateRangeInDays(startDate, endDate);

        // 평균 계산
        double avgViews = safeDivide(totalViews, totalVideos);
        double avgLikes = safeDivide(totalLikes, totalVideos);
        double avgComments = safeDivide(totalComments, totalVideos);
        double dailyAvgViews = safeDivide(totalViews, dateRange);
        double monthlyAvgViews = safeDivide(totalViews, Math.max(1, dateRange / 30.0));

        // 통계 DTO 생성
        YoutubeStatsResponse response = YoutubeStatsResponse.builder()
                .totalVideos(totalVideos)
                .avgViews(avgViews)
                .avgLikes(avgLikes)
                .avgComments(avgComments)
                .dailyAvgViews(dailyAvgViews)
                .monthlyAvgViews(monthlyAvgViews)
                .subscriberAgeRatio(youtubeApi.getAgeGroupRatio(accessToken, channelId, startDate, endDate))
                .subscriberGenderRatio(youtubeApi.getGenderRatio(accessToken, channelId, startDate, endDate))
                .subscriberChange(youtubeApi.getSubscriberChanges(accessToken, channelId))
                .subscribedVsNot(youtubeApi.getSubscribedStatusRatio(accessToken, channelId, startDate, endDate))
                .topVideos(youtubeApi.getTopVideos(accessToken, channelId, startDate, endDate))
                .topShorts(youtubeApi.getTopShorts(accessToken, channelId, startDate, endDate))
                .build();

        return response;
    }

    private double safeDivide(double numerator, double denominator) {
        return denominator == 0 ? 0 : numerator / denominator;
    }
}
