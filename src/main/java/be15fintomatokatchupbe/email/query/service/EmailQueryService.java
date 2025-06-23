package be15fintomatokatchupbe.email.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.email.query.dto.request.EmailSearchRequest;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionDTO;
import be15fintomatokatchupbe.email.query.dto.response.CampaignSatisfactionResponse;
import be15fintomatokatchupbe.email.query.mapper.EmailQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailQueryService {

    private final EmailQueryMapper emailQueryMapper;

    public CampaignSatisfactionResponse getCampaignSatisfaction(EmailSearchRequest emailSearchRequest) {

        List<CampaignSatisfactionDTO> responses = emailQueryMapper.getCampaignSatisfaction(emailSearchRequest);

        int totalList = emailQueryMapper.totalList(emailSearchRequest);

        int page = emailSearchRequest.getPage();
        int size = emailSearchRequest.getSize();

        return CampaignSatisfactionResponse.builder()
                .campaignSatisfaction(responses)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage((int) Math.ceil((double) totalList / size))
                        .size(size)
                        .build())
                .build();
    }
}
