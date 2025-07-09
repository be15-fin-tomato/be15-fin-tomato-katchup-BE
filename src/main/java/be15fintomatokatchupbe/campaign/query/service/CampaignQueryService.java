package be15fintomatokatchupbe.campaign.query.service;


import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.query.dto.mapper.*;
import be15fintomatokatchupbe.campaign.query.dto.request.CampaignResultRequest;
import be15fintomatokatchupbe.campaign.query.dto.request.PipelineSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.*;
import be15fintomatokatchupbe.campaign.query.mapper.CampaignQueryMapper;
import be15fintomatokatchupbe.campaign.query.dto.mapper.ListupFormDTO;
import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CampaignQueryService {
    private final CampaignQueryMapper campaignQueryMapper;

    public ListupSearchResponse getListupList(Long userId, PipelineSearchRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        int size = request.getSize();

        List<ListupCardDTO> listupList = campaignQueryMapper.findListupList(request, offset, size, PipelineStepConstants.LIST_UP);

        List<ListupCardResponse> response = new ArrayList<>();

        for(ListupCardDTO dto: listupList){
            List<String> influencerNameList = Optional.ofNullable(dto.getInfluencerNameInfo())
                    .map(s -> Arrays.stream(s.split(","))
                            .map(String::trim)
                            .toList())
                    .orElse(List.of());
            ListupCardResponse listupCardResponse = ListupCardResponse.builder()
                    .pipelineId(dto.getPipelineId())
                    .name(dto.getName())
                    .campaignName(dto.getCampaignName())
                    .clientCompanyName(dto.getClientCompanyName())
//                    .clientManagerName(dto.getClientManagerName())
                    .productName(dto.getProductName())
                    .userName(Collections.singletonList(dto.getUserName()))
                    .influencerList(influencerNameList)
                    .build();
            response.add(listupCardResponse);
        }



        int totalCount = campaignQueryMapper.countListup(request, PipelineStepConstants.LIST_UP);
        log.info("총 개수: {}", totalCount);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .size(size)
                .totalPage((int) Math.ceil((double) totalCount /size))
                .totalCount(totalCount)
                .build();

        return ListupSearchResponse.builder()
                .response(response)
                .pagination(pagination)
                .build();
    }

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
                    .campaignName(dto.getCampaignName())
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
//                    .campaignName()
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

    public RevenueSearchResponse getRevenueList(Long userId, PipelineSearchRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
        int size = request.getSize();

        // 1. userName을 제외한 유저 정보 조회 (배열로 돌아오는 얘들은 다른 쿼리에서 처리하기!)
        List<RevenueCardDTO> quotationList =
                campaignQueryMapper.findRevenueList(request, offset, size, PipelineStepConstants.REVENUE);

        // 광고 단가 합 가져오기

        List<RevenueCardResponse> response = new ArrayList<>();
        for(RevenueCardDTO dto: quotationList){
            List<String> userNameList = Optional.ofNullable(dto.getUserNameInfo())
                    .map(s -> Arrays.stream(s.split(","))
                            .map(String::trim)
                            .toList())
                    .orElse(List.of());

            Long totalAdPrice = campaignQueryMapper.findTotalAdPrice(dto.getPipelineId());
            RevenueCardResponse revenueCardResponse = RevenueCardResponse.builder()
                    .pipelineId(dto.getPipelineId())
                    .name(dto.getName())
                    .statusName(dto.getStatusName())
                    .clientCompanyName(dto.getClientCompanyName())
                    .clientManagerName(dto.getClientManagerName())
                    .productName(dto.getProductName())
                    .totalAdPrice(totalAdPrice)
                    .userName(userNameList)
                    .build();

            response.add(revenueCardResponse);
        }

        int totalCount = campaignQueryMapper.countPipeline(request, PipelineStepConstants.REVENUE);
        log.info("totalcount : {}",totalCount);

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .size(size)
                .totalPage((int) Math.ceil((double) totalCount /size))
                .totalCount(totalCount)
                .build();

        return
                RevenueSearchResponse.builder()
                        .response(response)
                        .pagination(pagination)
                        .build();
    }

    public QuotationDetailResponse getQuotationDetail(Long userId, Long pipelineId) {
        /* 폼 정보 가져오기 */
        /* 폼 가져오기 */
        QuotationFormDTO quotationFormDto = campaignQueryMapper.findQuotationDetail(pipelineId, PipelineStepConstants.QUOTATION);

        /* 인풀루언서 가져오기 */
        List<InfluencerInfo> influencerDto = campaignQueryMapper.findPipelineInfluencer(pipelineId);

        /* 담당자 가져오기 */
        List<UserInfo> userDto = campaignQueryMapper.findPipelineUser(pipelineId);

        /* 참고 목록 가져오기 */
        List<ReferenceInfo> referenceDto = campaignQueryMapper.findPipeReference(pipelineId, PipelineStepConstants.PROPOSAL);

        /* 의견 가져오기 */
        List<IdeaInfo> ideaDto = campaignQueryMapper.findPipeIdea(pipelineId);

        /* 조합하기 */
        QuotationFormResponse form = QuotationFormResponse.builder()
                .name(quotationFormDto.getName())
                .clientCompanyId(quotationFormDto.getClientCompanyId())
                .clientCompanyName(quotationFormDto.getClientCompanyName())
                .clientManagerId(quotationFormDto.getClientManagerId())
                .clientManagerName(quotationFormDto.getClientManagerName())
                .pipelineStatusId(quotationFormDto.getPipelineStatusId())
                .pipelineStatusName(quotationFormDto.getPipelineStatusName())
                .userList(userDto)
                .campaignId(quotationFormDto.getCampaignId())
                .campaignName(quotationFormDto.getCampaignName())
                .requestAt(quotationFormDto.getRequestAt())
                .presentAt(quotationFormDto.getPresentAt())
                .startedAt(quotationFormDto.getStartedAt())
                .endedAt(quotationFormDto.getEndedAt())
                .influencerList(influencerDto)
                .expectedRevenue(quotationFormDto.getExpectedRevenue())
                .availableQuantity(quotationFormDto.getAvailableQuantity())
                .expectedProfit(quotationFormDto.getExpectedProfit())
                .content(quotationFormDto.getContent())
                .notes(quotationFormDto.getNotes())
                .build();

        /* 응답하기 */
        return QuotationDetailResponse
                .builder()
                .form(form)
                .refrenceList(referenceDto)
                .ideaList(ideaDto)
                .build();
    }

    public ContractDetailResponse getContractDetail(Long userId, Long pipelineId) {
        /* 폼 정보 가져오기 */
        /* 폼 가져오기 */
        ContractFormDTO contractFormDto = campaignQueryMapper.findContractDetail(pipelineId, PipelineStepConstants.CONTRACT);

        /* 인플루언서 가져오기 */
        List<InfluencerInfo> influencerDto = campaignQueryMapper.findPipelineInfluencer(pipelineId);

        /* 담당자 가져오기 */
        List<UserInfo> userDto = campaignQueryMapper.findPipelineUser(pipelineId);

        /* 참고 목록 가져오기 */
        List<ReferenceInfo> referenceDto = campaignQueryMapper.findPipeReference(pipelineId, PipelineStepConstants.QUOTATION);

        /* 의견 가져오기 */
        List<IdeaInfo> ideaDto = campaignQueryMapper.findPipeIdea(pipelineId);

        /* 파일 목록 가져오기 */
        List<FileInfo> fileDto = campaignQueryMapper.findPipeFile(pipelineId);

        /* 조합하기 */
        ContractFormResponse form = ContractFormResponse.builder()
                .name(contractFormDto.getName())
                .clientCompanyId(contractFormDto.getClientCompanyId())
                .clientCompanyName(contractFormDto.getClientCompanyName())
                .clientManagerId(contractFormDto.getClientManagerId())
                .clientManagerName(contractFormDto.getClientManagerName())
                .userList(userDto)
                .campaignId(contractFormDto.getCampaignId())
                .campaignName(contractFormDto.getCampaignName())
                .pipelineStatusId(contractFormDto.getPipelineStatusId())
                .pipelineStatusName(contractFormDto.getPipelineStatusName())
                .requestAt(contractFormDto.getRequestAt())
                .presentAt(contractFormDto.getPresentAt())
                .startedAt(contractFormDto.getStartedAt())
                .endedAt(contractFormDto.getEndedAt())
                .notes(contractFormDto.getNotes())
                .content(contractFormDto.getContent())
                .influencerList(influencerDto)
                .expectedRevenue(contractFormDto.getExpectedRevenue())
                .availableQuantity(contractFormDto.getAvailableQuantity())
                .expectedProfit(contractFormDto.getExpectedProfit())
                .productPrice(contractFormDto.getProductPrice())
                .build();

        /* 응답하기 */
        return ContractDetailResponse.builder()
                .form(form)
                .fileList(fileDto)
                .referenceList(referenceDto)
                .ideaList(ideaDto)
                .build();
    }

    public RevenueDetailResponse getRevenueDetail(Long userId, Long pipelineId) {
        RevenueFormDTO revenueFormDto = campaignQueryMapper.findRevenueDetail(pipelineId, PipelineStepConstants.REVENUE);

        /* 담당자 가져오기 */
        List<UserInfo> userDto = campaignQueryMapper.findPipelineUser(pipelineId);

        /* 인플루언서 가져오기 (URL도)*/
        List<InfluencerRevenueInfo> influencerDto = campaignQueryMapper.findPipelineRevenueInfluencer(pipelineId);

        /* 참고 목록 가져오기 */
        List<ReferenceInfo> referenceDto = campaignQueryMapper.findPipeReference(pipelineId, PipelineStepConstants.CONTRACT);

        /* 의견 가져오기 */
        List<IdeaInfo> ideaDto = campaignQueryMapper.findPipeIdea(pipelineId);

        /* 파일 목록 가져오기 */
        List<FileInfo> fileDto = campaignQueryMapper.findPipeFile(pipelineId);
        log.info("판매 수량: {}", revenueFormDto.getSalesQuantity());
        /* 조합하기 */
        RevenueFormResponse form = RevenueFormResponse.builder()
                .name(revenueFormDto.getName())
                .clientCompanyId(revenueFormDto.getClientCompanyId())
                .clientCompanyName(revenueFormDto.getClientCompanyName())
                .clientManagerId(revenueFormDto.getClientManagerId())
                .clientManagerName(revenueFormDto.getClientManagerName())
                .userList(userDto)
                .campaignId(revenueFormDto.getCampaignId())
                .campaignName(revenueFormDto.getCampaignName())
                .pipelineStatusId(revenueFormDto.getPipelineStatusId())
                .pipelineStatusName(revenueFormDto.getPipelineStatusName())
                .requestAt(revenueFormDto.getRequestAt())
                .presentAt(revenueFormDto.getPresentAt())
                .startedAt(revenueFormDto.getStartedAt())
                .endedAt(revenueFormDto.getEndedAt())
                .notes(revenueFormDto.getNotes())
                .content(revenueFormDto.getContent())
                .influencerList(influencerDto)
                .productPrice(revenueFormDto.getProductPrice())
                .salesQuantity(revenueFormDto.getSalesQuantity())
                .build();

        return RevenueDetailResponse.builder()
                .form(form)
                .fileList(fileDto)
                .referenceList(referenceDto)
                .ideaList(ideaDto)
                .build();
    }

    public CampaignDetailWithTimelineResponse getCampaignDetailWithTimeline(Long campaignId) {
        // 1. 캠페인 기본 정보 조회
        CampaignDetailDto detail = campaignQueryMapper.selectCampaignDetail(campaignId);
        if (detail == null) {
            return null;
        }

        // 날짜 포맷터 선언
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 2. 캠페인 유저 리스트 조회
        List<User> userList = campaignQueryMapper.selectCampaignUserList(detail.getClientCompanyId());
        detail.setUserList(userList);

        // 3. 캠페인 카테고리 리스트 조회
        List<Long> categoryList = campaignQueryMapper.selectCampaignCategoryList(campaignId);
        detail.setCategoryList(categoryList);

        Long totalExpectedRevenue = campaignQueryMapper.selectTotalExpectedRevenue(campaignId);
        detail.setExpectedRevenue(totalExpectedRevenue);


        BigDecimal avgProfitMargin = campaignQueryMapper.selectAverageExpectedProfitMargin(campaignId);

        if (avgProfitMargin != null) {
            detail.setExpectedProfitMargin(
                    avgProfitMargin.multiply(BigDecimal.valueOf(100))
                            .setScale(0, RoundingMode.HALF_UP)
            );
        } else {
            detail.setExpectedProfitMargin(BigDecimal.ZERO);
        }

        String notes = campaignQueryMapper.selectCampaignNotes(campaignId);
        detail.setNotes(notes);

        // 4. 파이프라인 타임라인 조회
        List<PipelineTimelineDto> timeline = campaignQueryMapper.selectPipelineTimeline(campaignId);

        return new CampaignDetailWithTimelineResponse(detail, timeline);


    }

    public CampaignSearchResponse findCampaignList(String keyword, Long clientCompanyId) {
        List<CampaignSimpleDto> campaignList = campaignQueryMapper.findCampaignList(keyword, clientCompanyId);

        return CampaignSearchResponse.builder()
                .campaignList(campaignList)
                .build();
    }

    public List<CampaignListResponse> getPagedCampaigns(int page, int size) {
        int offset = (page - 1) * size;

        List<CampaignListResponse> campaigns = campaignQueryMapper.findPagedCampaigns(size, offset);
        List<Long> campaignIds = campaigns.stream()
                .map(CampaignListResponse::getCampaignId)
                .distinct()
                .toList();

        if (!campaignIds.isEmpty()) {
            List<PipelineStepStatusDto> steps = campaignQueryMapper.findPipelineStepsByCampaignIds(campaignIds);

            Map<Long, List<PipelineStepDto>> stepMap = steps.stream()
                    .collect(Collectors.groupingBy(
                            PipelineStepStatusDto::getCampaignId,
                            Collectors.mapping(
                                    s -> new PipelineStepDto(s.getStepType(), s.getStartedAt()),
                                    Collectors.toList()
                            )
                    ));

            for (CampaignListResponse dto : campaigns) {
                List<PipelineStepDto> stepList = stepMap.getOrDefault(dto.getCampaignId(), Collections.emptyList());
                dto.setPipelineSteps(stepList);

                // 사후관리는 제외 (총 6단계만 계산)
                long completed = stepList.stream().filter(s -> s.getStartedAt() != null).count();
                dto.setSuccessProbability((int) ((completed / 6.0) * 100));
            }
        }

        return campaigns;
    }

    public QuotationReferenceListResponse getQuotationReferenceList(Long campaignId) {
        List<ReferenceDto> contractReferenceList = campaignQueryMapper.getReferenceList(campaignId, PipelineStepConstants.QUOTATION);

        return QuotationReferenceListResponse.builder()
                .referenceList(contractReferenceList)
                .build();
    }

    public ContractReferenceListResponse getContractReferenceList(Long campaignId) {
        List<ReferenceDto> contractReferenceList = campaignQueryMapper.getReferenceList(campaignId, PipelineStepConstants.CONTRACT);

        return ContractReferenceListResponse.builder()
                .referenceList(contractReferenceList)
                .build();
    }

    public CampaignResultListResponse findCampaignResultList(CampaignResultRequest request) {
        int page = request.getPage() != null ? request.getPage() : 1;
        int size = request.getSize() != null ? request.getSize() : 6;

        int offset = (page - 1) * size;

        int total = campaignQueryMapper.countCampaignResultList(request);

        List<CampaignResultResponse> rawResultList =
                campaignQueryMapper.findCampaignResultList(
                        request,    // @Param("request")로 매퍼에 전달
                        offset,     // @Param("offset")로 매퍼에 전달
                        size,       // @Param("size")로 매퍼에 전달
                        request.getSortBy(),
                        request.getSortOrder()
                );

        List<CampaignResultResponse> finalResultList = rawResultList;

        return CampaignResultListResponse.builder()
                .data(finalResultList)
                .total(total)
                .build();
    }

    public ListupDetailResponse getListupDetail(Long userId, Long pipelineId) {
        /* 인플루언서 가져오기 */
        List<InfluencerInfo> influencerList = campaignQueryMapper.findPipelineInfluencer(pipelineId);

        /* 폼 가져오기 */
        ListupFormDTO listupFormDto = campaignQueryMapper.findListupDetail(pipelineId, PipelineStepConstants.LIST_UP);

        return ListupDetailResponse.builder()
                .campaignId(listupFormDto.getCampaignId())
                .campaignName(listupFormDto.getCampaignName())
                .clientCompanyId(listupFormDto.getClientCompanyId())
                .clientCompanyName(listupFormDto.getClientCompanyName())
                .influencerList(influencerList)
                .build();
    }
}


