package be15fintomatokatchupbe.campaign.query.service;


import be15fintomatokatchupbe.campaign.query.dto.request.ProposalSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalCardResponse;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalSearchResponse;
import be15fintomatokatchupbe.campaign.query.mapper.CampaignQueryMapper;
import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CampaignQueryService {
    private final CampaignQueryMapper campaignQueryMapper;

    public ProposalSearchResponse getProposalList(Long userId, ProposalSearchRequest request) {
        log.info(request.getPage() + " " + request.getSize());
        int offset = (request.getPage() - 1) * request.getSize();
        int size = request.getSize();

        // 1. 매퍼 호출
        List<ProposalCardResponse> proposalCardResponse = campaignQueryMapper.findProposals(request, offset, size);

        log.info(proposalCardResponse.toString());

        // 2. 총 개수 세주기
        int totalCount = campaignQueryMapper.countProposals(request);

        // 페이지네이션 만들기
        Pagination pagination = Pagination
                .builder()
                .currentPage(request.getPage())
                .size(size)
                .totalPage(totalCount)
                .build();

        return ProposalSearchResponse
                .builder()
                .response(proposalCardResponse)
                .pagination(pagination)
                .build();
    }
}
