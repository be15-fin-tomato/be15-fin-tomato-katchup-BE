package be15fintomatokatchupbe.email.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.email.query.dto.request.EmailSearchRequest;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionDTO;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponse;
import be15fintomatokatchupbe.email.query.mapper.EmailQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class EmailQueryServiceTest {

    @Mock
    private EmailQueryMapper emailQueryMapper;

    @InjectMocks
    private EmailQueryService emailQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCampaignSatisfaction_success() {
        // given
        EmailSearchRequest request = new EmailSearchRequest();
        request.setPage(1);
        request.setSize(10);

        CampaignSatisfactionDTO dto = new CampaignSatisfactionDTO();
        dto.setCampaignName("캠페인 A");

        List<CampaignSatisfactionDTO> expectedList = new ArrayList<>();
        expectedList.add(dto);

        when(emailQueryMapper.getCampaignSatisfaction(request)).thenReturn(expectedList);
        when(emailQueryMapper.totalList(request)).thenReturn(1);

        // when
        CampaignSatisfactionResponse result = emailQueryService.getCampaignSatisfaction(request);

        // then
        assertNotNull(result);
        assertEquals(1, result.getCampaignSatisfaction().size());
        assertEquals("캠페인 A", result.getCampaignSatisfaction().get(0).getCampaignName());

        Pagination pagination = result.getPagination();
        assertEquals(1, pagination.getCurrentPage());
        assertEquals(1, pagination.getTotalPage());
        assertEquals(10, pagination.getSize());
    }

    @Test
    void getCampaignSatisfaction_emptyResult() {
        // given
        EmailSearchRequest request = new EmailSearchRequest();
        request.setPage(1);
        request.setSize(10);

        when(emailQueryMapper.getCampaignSatisfaction(request)).thenReturn(new ArrayList<>());
        when(emailQueryMapper.totalList(request)).thenReturn(0);

        // when
        CampaignSatisfactionResponse result = emailQueryService.getCampaignSatisfaction(request);

        // then
        assertNotNull(result);
        assertTrue(result.getCampaignSatisfaction().isEmpty());
        assertEquals(0, result.getPagination().getTotalPage());
    }
}
