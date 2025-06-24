package be15fintomatokatchupbe.campaign.query.service;


import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.query.dto.ProposalCardDTO;
import be15fintomatokatchupbe.campaign.query.dto.request.ProposalSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalCardResponse;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalSearchResponse;
import be15fintomatokatchupbe.campaign.query.mapper.CampaignQueryMapper;
import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CampaignQueryService {
    private final CampaignQueryMapper campaignQueryMapper;

    public ProposalSearchResponse getProposalList(Long userId, ProposalSearchRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        int size = request.getSize();

        // 1. flat DTO 리스트 조회
        List<ProposalCardDTO> flatList =
                campaignQueryMapper.findProposals(request, offset, size, PipelineStepConstants.PROPOSAL);

        // 2. 그룹화 처리
        Map<Long, ProposalCardResponse> grouped = new LinkedHashMap<>();

        for (ProposalCardDTO dto : flatList) {
            ProposalCardResponse existing = grouped.get(dto.getPipelineId());

            if (existing == null) {
                ProposalCardResponse response = ProposalCardResponse.builder()
                        .pipelineId(dto.getPipelineId())
                        .name(dto.getName())
                        .statusId(dto.getStatusId())
                        .clientCompanyName(dto.getClientCompanyName())
                        .clientManagerName(dto.getClientManagerName())
                        .requestAt(dto.getRequestAt())
                        .presentAt(dto.getPresentAt())
                        .userName(new ArrayList<>(List.of(dto.getUserName())))
                        .build();
                grouped.put(dto.getPipelineId(), response);
            } else {
                existing.getUserName().add(dto.getUserName());
            }
        }

        List<ProposalCardResponse> groupedList = new ArrayList<>(grouped.values());

        // 3. 총 개수 조회
        int totalCount = campaignQueryMapper.countProposals(request, PipelineStepConstants.PROPOSAL);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .size(size)
                .totalPage(totalCount)
                .build();

        return ProposalSearchResponse.builder()
                .response(groupedList)
                .pagination(pagination)
                .build();
    }

}
