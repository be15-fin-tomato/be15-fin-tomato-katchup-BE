package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.AnalyticsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.ChannelStatsResponse;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.ChannelStatsResponse.Item;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService.ChannelStatsResponse.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YoutubeOAuthQueryServiceTest {

    @Mock
    private WebClient webClient;

    private YoutubeOAuthQueryService youtubeService;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setup() {
        youtubeService = new YoutubeOAuthQueryService(webClient);

        lenient().when(webClient.get()).thenReturn(uriSpec);
        lenient().when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        lenient().when(headersSpec.header(any(String.class), any(String.class))).thenReturn(headersSpec);
        lenient().when(headersSpec.retrieve()).thenReturn(responseSpec);
    }


    @Test
    void getTotalViews_returnsSumOfViews() {
        AnalyticsResponse response = new AnalyticsResponse();
        response.setRows(List.of(
                List.of("2025-06-01", 100L),
                List.of("2025-06-02", 200L)
        ));
        when(responseSpec.bodyToMono(AnalyticsResponse.class)).thenReturn(Mono.just(response));

        long totalViews = youtubeService.getTotalViews("access-token", "channelId", "2025-06-01", "2025-06-02");
        assertEquals(300L, totalViews);
    }

    @Test
    void getTotalVideoCount_returnsCorrectVideoCount() {
        Statistics stats = new Statistics();
        stats.setVideoCount(42);

        Item item = new Item();
        item.setStatistics(stats);

        ChannelStatsResponse response = new ChannelStatsResponse();
        response.setItems(List.of(item));

        when(responseSpec.bodyToMono(ChannelStatsResponse.class)).thenReturn(Mono.just(response));

        int count = youtubeService.getTotalVideoCount("access-token", "channelId");
        assertEquals(42, count);
    }

    @Test
    void getDateRangeInDays_returnsCorrectRange() {
        int result = youtubeService.getDateRangeInDays("2025-06-01", "2025-06-05");
        assertEquals(5, result);
    }
}
