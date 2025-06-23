package be15fintomatokatchupbe.oauth.query.service;

import be15fintomatokatchupbe.oauth.query.controller.YoutubeOAuthQueryController;
import be15fintomatokatchupbe.oauth.query.service.YoutubeOAuthQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(YoutubeOAuthQueryController.class)
class YoutubeOAuthQueryControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YoutubeOAuthQueryService youtubeOAuthQueryService;

    @Test
    void authorize_shouldRedirect() throws Exception {
        String redirectUrl = "https://example.com/oauth2";
        given(youtubeOAuthQueryService.buildAuthorizationUrl()).willReturn(redirectUrl);

        mockMvc.perform(get("/oauth2/authorize/youtube"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectUrl));
    }

    // 위의 통합 테스트에서 쓴 나머지 테스트도 똑같이 적용 가능
}
