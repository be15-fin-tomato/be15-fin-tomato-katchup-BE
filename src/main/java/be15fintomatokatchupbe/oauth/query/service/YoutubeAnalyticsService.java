package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YoutubeAnalyticsService {

    private final YoutubeOAuthQueryService youtubeApi;

    public YoutubeStatsResponse getYoutubeStats(String accessToken, String channelId, String startDate, String endDate) {
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

        // 전체 통계 응답 구성
        return YoutubeStatsResponse.builder()
                .totalVideos(totalVideos)
                .avgViews(avgViews)
                .avgLikes(avgLikes)
                .avgComments(avgComments)
                .dailyAvgViews(dailyAvgViews)
                .monthlyAvgViews(monthlyAvgViews)
                .subscriberAgeRatio(
                        youtubeApi.getAgeGroupRatio(accessToken, channelId, startDate, endDate))
                .subscriberGenderRatio(
                        youtubeApi.getGenderRatio(accessToken, channelId, startDate, endDate))
                .subscriberChange(
                        youtubeApi.getSubscriberChanges(accessToken, channelId))
                .subscribedVsNot(
                        youtubeApi.getSubscribedStatusRatio(accessToken, channelId, startDate, endDate))
                .topVideos(
                        youtubeApi.getTopVideos(accessToken, channelId, startDate, endDate))
                .topShorts(
                        youtubeApi.getTopShorts(accessToken, channelId, startDate, endDate))
                .build();
    }

    private double safeDivide(double numerator, double denominator) {
        return denominator == 0 ? 0 : numerator / denominator;
    }
}
