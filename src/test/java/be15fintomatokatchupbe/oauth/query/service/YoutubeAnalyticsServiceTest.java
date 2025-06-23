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

    @BeforeEach
    void setUp() {
        when(youtubeApi.getTotalVideoCount("token", "channelId")).thenReturn(10);
        when(youtubeApi.getTotalViews("token", "channelId", "2025-06-01", "2025-06-30")).thenReturn(1000L);
        when(youtubeApi.getTotalLikes("token", "channelId", "2025-06-01", "2025-06-30")).thenReturn(300L);
        when(youtubeApi.getTotalComments("token", "channelId", "2025-06-01", "2025-06-30")).thenReturn(50L);
        when(youtubeApi.getDateRangeInDays("2025-06-01", "2025-06-30")).thenReturn(30);

        when(youtubeApi.getAgeGroupRatio("token", "channelId", "2025-06-01", "2025-06-30"))
                .thenReturn(Map.of("18-24", 60.0, "25-34", 40.0));

        when(youtubeApi.getGenderRatio("token", "channelId", "2025-06-01", "2025-06-30"))
                .thenReturn(Map.of("male", 55.0, "female", 45.0));

        when(youtubeApi.getSubscriberChanges("token", "channelId"))
                .thenReturn(Map.of("daily", 5, "weekly", 20, "monthly", 60));

        when(youtubeApi.getSubscribedStatusRatio("token", "channelId", "2025-06-01", "2025-06-30"))
                .thenReturn(Map.of("subscribed", 40.0, "notSubscribed", 60.0));

        when(youtubeApi.getTopVideos("token", "channelId", "2025-06-01", "2025-06-30"))
                .thenReturn(List.of());

        when(youtubeApi.getTopShorts("token", "channelId", "2025-06-01", "2025-06-30"))
                .thenReturn(List.of());
    }

    @Test
    void getYoutubeStats_returnsCorrectAggregatedStats() {
        YoutubeStatsResponse result = analyticsService.getYoutubeStats("token", "channelId", "2025-06-01", "2025-06-30");

        assertEquals(10, result.getTotalVideos());
        assertEquals(100.0, result.getAvgViews());
        assertEquals(30.0, result.getAvgLikes());
        assertEquals(5.0, result.getAvgComments());
        assertEquals(1000.0, result.getMonthlyAvgViews(), 0.01);
        assertEquals(1000.0 / 30, result.getDailyAvgViews(), 0.01);
        assertEquals(60.0, result.getSubscriberAgeRatio().get("18-24"));
        assertEquals(45.0, result.getSubscriberGenderRatio().get("female"));
        assertEquals(20, result.getSubscriberChange().get("weekly"));
        assertEquals(60.0, result.getSubscribedVsNot().get("notSubscribed"));
    }
}
