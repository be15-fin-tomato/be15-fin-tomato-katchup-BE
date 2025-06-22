package be15fintomatokatchupbe.dashboard.query.service;

import be15fintomatokatchupbe.dashboard.query.dto.response.CampaignResponse;
import be15fintomatokatchupbe.dashboard.query.mapper.PlatformDashboardQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlatformDashboardQueryServiceTest {
    @Mock
    private PlatformDashboardQueryMapper platformDashboardQueryMapper;

    @InjectMocks
    private PlatformDashboardQueryService platformDashboardQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCampaignList_shouldReturnCampaigns() {
        Long influencerId = 1L;
        List<CampaignResponse> expected = List.of(
                CampaignResponse.builder()
                        .clientCompanyName("카카오엔터프라이즈")
                        .campaignName("카카오 AI 패키지 홍보")
                        .productName("AI 번역기 Pro")
                        .youtubeLink("https://youtube.com/example1")
                        .instagramLink("https://instagram.com/example1")
                        .build()
        );

        when(platformDashboardQueryMapper.findCampaignsByInfluencerId(influencerId)).thenReturn(expected);

        List<CampaignResponse> result = platformDashboardQueryService.getCampaignList(influencerId);

        assertEquals(1, result.size());
        assertEquals("카카오엔터프라이즈", result.get(0).getClientCompanyName());
        assertEquals("카카오 AI 패키지 홍보", result.get(0).getCampaignName());
        assertEquals("AI 번역기 Pro", result.get(0).getProductName());
        verify(platformDashboardQueryMapper).findCampaignsByInfluencerId(influencerId);
    }
}
