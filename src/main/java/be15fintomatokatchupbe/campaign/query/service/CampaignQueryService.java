package be15fintomatokatchupbe.campaign.query.service;


import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.query.dto.mapper.ContractCardDTO;
import be15fintomatokatchupbe.campaign.query.dto.mapper.ProposalCardDTO;
import be15fintomatokatchupbe.campaign.query.dto.mapper.QuotationCardDTO;
import be15fintomatokatchupbe.campaign.query.dto.request.PipelineSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.*;
import be15fintomatokatchupbe.campaign.query.mapper.CampaignQueryMapper;
import be15fintomatokatchupbe.common.dto.Pagination;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class CampaignQueryService {
    private final CampaignQueryMapper campaignQueryMapper;

    public ProposalSearchResponse getProposalList(Long userId, PipelineSearchRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        int size = request.getSize();

        // 1. flat DTO 리스트 조회
        List<ProposalCardDTO> proposalList =
                campaignQueryMapper.findPipelineList(request, offset, size, PipelineStepConstants.PROPOSAL);

        List<ProposalCardResponse> response = new ArrayList<>();

        for(ProposalCardDTO dto: proposalList){
            List<String> userNameList = Optional.ofNullable(dto.getUserNameInfo())
                    .map(s -> Arrays.stream(s.split(","))
                            .map(String::trim)
                            .toList())
                    .orElse(List.of());
            ProposalCardResponse proposalCardResponse = ProposalCardResponse.builder()
                    .pipelineId(dto.getPipelineId())
                    .name(dto.getName())
                    .statusName(dto.getStatusName())
                    .clientManagerName(dto.getClientManagerName())
                    .clientCompanyName(dto.getClientCompanyName())
                    .userName(userNameList)
                    .presentAt(dto.getPresentAt())
                    .requestAt(dto.getRequestAt())
                    .build();

            response.add(proposalCardResponse);
        }

        // 3. 총 개수 조회
        int totalCount = campaignQueryMapper.countPipeline(request, PipelineStepConstants.PROPOSAL);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .size(size)
                .totalPage((int) Math.ceil((double) totalCount /size))
                .totalCount(totalCount)
                .build();

        return ProposalSearchResponse.builder()
                .response(response)
                .pagination(pagination)
                .build();
    }

    public QuotationSearchResponse getQuotationList(Long userId, PipelineSearchRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        int size = request.getSize();

        // 1. userName을 제외한 유저 정보 조회 (배열로 돌아오는 얘들은 다른 쿼리에서 처리하기!)
        List<QuotationCardDTO> quotationList =
                campaignQueryMapper.findQuotationList(request, offset, size, PipelineStepConstants.QUOTATION);

        List<QuotationCardResponse> response = new ArrayList<>();
        // 2. 해쉬 셋에 {pipelineId}: {DTO} 형태로 저장하기
        for(QuotationCardDTO dto: quotationList){
            List<String> userNameList = Optional.ofNullable(dto.getUserNameInfo())
                    .map(s -> Arrays.stream(s.split(","))
                            .map(String::trim)
                            .toList())
                    .orElse(List.of());
            QuotationCardResponse quotationCardResponse = QuotationCardResponse.builder()
                    .pipelineId(dto.getPipelineId())
                    .name(dto.getName())
                    .statusName(dto.getStatusName())
                    .clientCompanyName(dto.getClientCompanyName())
                    .clientManagerName(dto.getClientManagerName())
                    .productName(dto.getProductName())
                    .expectedRevenue(dto.getExpectedRevenue())
                    .userName(userNameList)
                    .build();

            response.add(quotationCardResponse);
        }

        int totalCount = campaignQueryMapper.countPipeline(request, PipelineStepConstants.QUOTATION);
        log.info("totalcount : {}",totalCount);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .size(size)
                .totalPage((int) Math.ceil((double) totalCount /size))
                .totalCount(totalCount)
                .build();

        return
                QuotationSearchResponse.builder()
                        .response(response)
                        .pagination(pagination)
                        .build();
    }

    public ContractSearchResponse getContractList(Long userId, PipelineSearchRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        int size = request.getSize();

        // 1. userName을 제외한 유저 정보 조회 (배열로 돌아오는 얘들은 다른 쿼리에서 처리하기!)
        List<ContractCardDTO> quotationList =
                campaignQueryMapper.findContractList(request, offset, size, PipelineStepConstants.CONTRACT);

        List<ContractCardResponse> response = new ArrayList<>();
        // 2. 해쉬 셋에 {pipelineId}: {DTO} 형태로 저장하기
        for(ContractCardDTO dto: quotationList){
            List<String> userNameList = Optional.ofNullable(dto.getUserNameInfo())
                    .map(s -> Arrays.stream(s.split(","))
                            .map(String::trim)
                            .toList())
                    .orElse(List.of());

            ContractCardResponse contractCardResponse = ContractCardResponse.builder()
                    .pipelineId(dto.getPipelineId())
                    .name(dto.getName())
                    .statusName(dto.getStatusName())
                    .clientCompanyName(dto.getClientCompanyName())
                    .clientManagerName(dto.getClientManagerName())
                    .productName(dto.getProductName())
                    .expectedRevenue(dto.getExpectedRevenue())
                    .userName(userNameList)
                    .build();

            response.add(contractCardResponse);
        }

        int totalCount = campaignQueryMapper.countPipeline(request, PipelineStepConstants.CONTRACT);
        log.info("totalcount : {}",totalCount);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .size(size)
                .totalPage((int) Math.ceil((double) totalCount /size))
                .totalCount(totalCount)
                .build();

        return
                ContractSearchResponse.builder()
                        .response(response)
                        .pagination(pagination)
                        .build();
    }
}
