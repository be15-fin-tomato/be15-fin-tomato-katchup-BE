package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.oauth.query.dto.response.YoutubeStatsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YoutubeAnalyticsServiceTest {

    @Mock
    private YoutubeOAuthQueryService youtubeApi;

    @InjectMocks
    private YoutubeAnalyticsService analyticsService;

    private final String token = "token";
    private final String channelId = "channelId";
    private final String startDate = "2025-06-01";
    private final String endDate = "2025-06-30";

    @BeforeEach
    void setUp() {
        // 기본 통계 mock
        when(youtubeApi.getTotalVideoCount(token, channelId)).thenReturn(10);
        when(youtubeApi.getTotalViews(token, channelId, startDate, endDate)).thenReturn(1000L);
        when(youtubeApi.getTotalLikes(token, channelId, startDate, endDate)).thenReturn(300L);
        when(youtubeApi.getTotalComments(token, channelId, startDate, endDate)).thenReturn(50L);
        when(youtubeApi.getDateRangeInDays(startDate, endDate)).thenReturn(30);

        // 구독자 통계 mock
        when(youtubeApi.getAgeGroupRatio(token, channelId, startDate, endDate))
                .thenReturn(Map.of("18-24", 60.0, "25-34", 40.0));
        when(youtubeApi.getGenderRatio(token, channelId, startDate, endDate))
                .thenReturn(Map.of("male", 55.0, "female", 45.0));
        when(youtubeApi.getSubscriberChanges(token, channelId))
                .thenReturn(Map.of("daily", 5, "weekly", 20, "monthly", 60));
        when(youtubeApi.getSubscribedStatusRatio(token, channelId, startDate, endDate))
                .thenReturn(Map.of("subscribed", 40.0, "notSubscribed", 60.0));

        // 인기 콘텐츠 mock
        when(youtubeApi.getTopVideos(token, channelId, startDate, endDate)).thenReturn(List.of());
        when(youtubeApi.getTopShorts(token, channelId, startDate, endDate)).thenReturn(List.of());
    }

    @Test
    void getYoutubeStats_returnsCorrectAggregatedStats() {
        YoutubeStatsResponse stats = analyticsService.getYoutubeStats(token, channelId, startDate, endDate);

        // 기본 통계
        assertEquals(10, stats.getTotalVideos());
        assertEquals(100.0, stats.getAvgViews());
        assertEquals(30.0, stats.getAvgLikes());
        assertEquals(5.0, stats.getAvgComments());
        assertEquals(1000.0, stats.getMonthlyAvgViews());
        assertEquals(1000.0 / 30, stats.getDailyAvgViews(), 0.01);

        // 구독자 통계
        assertEquals(60.0, stats.getSubscriberAgeRatio().get("18-24"));
        assertEquals(45.0, stats.getSubscriberGenderRatio().get("female"));
        assertEquals(20, stats.getSubscriberChange().get("weekly"));
        assertEquals(60.0, stats.getSubscribedVsNot().get("notSubscribed"));
    }
}
