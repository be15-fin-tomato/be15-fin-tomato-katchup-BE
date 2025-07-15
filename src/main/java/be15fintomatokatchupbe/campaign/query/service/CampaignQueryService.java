package be15fintomatokatchupbe.campaign.query.service;


import be15fintomatokatchupbe.campaign.command.domain.aggregate.constant.PipelineStepConstants;
import be15fintomatokatchupbe.campaign.exception.CampaignErrorCode;
import be15fintomatokatchupbe.campaign.query.dto.request.*;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalReferenceListResponse;
import be15fintomatokatchupbe.campaign.query.dto.mapper.*;
import be15fintomatokatchupbe.campaign.query.dto.response.*;
import be15fintomatokatchupbe.campaign.query.mapper.CampaignQueryMapper;
import be15fintomatokatchupbe.campaign.query.dto.mapper.ListupFormDTO;
import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.influencer.query.dto.response.CategoryDto;
import be15fintomatokatchupbe.influencer.query.mapper.InfluencerMapper;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CampaignQueryService {
    private final CampaignQueryMapper campaignQueryMapper;
    private final InfluencerMapper influencerQueryMapper;
    private final ChatClient chatClient;

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
        List<ReferenceDto> referenceDto = campaignQueryMapper.getReferenceList(quotationFormDto.getCampaignId(), PipelineStepConstants.PROPOSAL);

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
        List<ReferenceDto> referenceDto = campaignQueryMapper.getReferenceList(contractFormDto.getCampaignId(), PipelineStepConstants.QUOTATION);

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
        List<ReferenceDto> referenceDto = campaignQueryMapper.getReferenceList(revenueFormDto.getCampaignId(), PipelineStepConstants.CONTRACT);

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

        // 2. 캠페인 유저 리스트 조회
        List<User> userList = campaignQueryMapper.selectCampaignUserList(detail.getCampaignId());
        detail.setUserList(userList);

        // 3. 캠페인 카테고리 리스트 조회
        List<Long> categoryList = campaignQueryMapper.selectCampaignCategoryList(campaignId);
        detail.setCategoryList(categoryList);

        Long totalExpectedRevenue = campaignQueryMapper.selectTotalExpectedRevenue(campaignId);
        detail.setExpectedRevenue(totalExpectedRevenue);


        float avgProfitMargin = campaignQueryMapper.selectAverageExpectedProfitMargin(campaignId);
        detail.setExpectedProfitMargin(avgProfitMargin);

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

    public CampaignListResponse getPagedCampaigns(int page, int size, ContractListRequest request) {
        int offset = (page - 1) * size;

        List<CampaignListsDTO> campaigns = campaignQueryMapper.findPagedCampaigns(size, offset, request);
        if (campaigns.isEmpty()) {
            Pagination pagination = Pagination.builder()
                    .currentPage(page)
                    .size(size)
                    .totalPage(0)
                    .totalCount(0)
                    .build();

            return CampaignListResponse.builder()
                    .campaignList(Collections.emptyList())
                    .pagination(pagination)
                    .build();
        }

        List<Long> campaignIds = campaigns.stream()
                .map(CampaignListsDTO::getCampaignId)
                .collect(Collectors.toList());

        List<PipelineStepsDto> stepsList = campaignQueryMapper.findPipelineStepsGroupedByCampaignIds(campaignIds);

        Map<Long, List<PipelineStepsDto>> stepMap = stepsList.stream()
                .collect(Collectors.groupingBy(PipelineStepsDto::getCampaignId));

        for (CampaignListsDTO campaign : campaigns) {
            List<PipelineStepsDto> steps = stepMap.getOrDefault(campaign.getCampaignId(), new ArrayList<>());
            campaign.setPipelineSteps(steps);
        }

        int totalCount = campaignQueryMapper.getTotalSize(request);

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .size(size)
                .totalPage((int) Math.ceil((double) totalCount / size))
                .totalCount(totalCount)
                .build();

        return CampaignListResponse.builder()
                .campaignList(campaigns)
                .pagination(pagination)
                .build();
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
        int clientPage = request.getPage() != null ? request.getPage() : 1;
        int clientSize = request.getSize() != null ? request.getSize() : 10; // 프론트 size와 통일
        int clientOffset = (clientPage - 1) * clientSize;

        // 1. 전체 필터링 조건에 맞는 "베이스 파이프라인" 데이터 조회
        // 이 쿼리에는 LIMIT/OFFSET이 없어야 합니다 (XML에서 제거했으므로).
        // 백엔드에서 모든 조건을 만족하는 파이프라인 기본 정보를 가져옵니다.
        request.setOffset(0);
        request.setSize(Integer.MAX_VALUE); // 모든 데이터를 가져오도록 유지

        List<CampaignResultResponse> baseList =
                campaignQueryMapper.findCampaignResultList(request);

        // 2. pipelineId 모으기
        List<Long> pipelineIds = baseList.stream()
                .map(CampaignResultResponse::getPipelineId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 3. 최종 카드 리스트 만들기 (인플루언서 정보 병합)
        List<CampaignResultResponse> finalList = new ArrayList<>();

        if (!pipelineIds.isEmpty()) {
            List<CampaignInfluencerInfo> infList =
                    campaignQueryMapper.findInfluencerInfoByPipelineIds(pipelineIds);

            Map<Long, List<CampaignInfluencerInfo>> infMap = infList.stream()
                    .collect(Collectors.groupingBy(CampaignInfluencerInfo::getPipelineId));

            for (CampaignResultResponse base : baseList) {
                List<CampaignInfluencerInfo> matched = infMap.get(base.getPipelineId());

                if (matched == null || matched.isEmpty()) {
                    // 인플루언서 정보가 없는 경우에도 추가
                    finalList.add(base);
                } else {
                    for (CampaignInfluencerInfo inf : matched) {
                        CampaignResultResponse card = new CampaignResultResponse();
                        BeanUtils.copyProperties(base, card);
                        card.setInfluencerName(inf.getInfluencerName());
                        card.setPipelineInfluencerId(inf.getPipelineInfluencerId());
                        finalList.add(card);
                    }
                }
            }
        } else {
            finalList = baseList; // baseList가 비어있지 않은 경우만 해당
        }

        // 4. 인플루언서 정보 병합된 최종 리스트에 대한 페이징 적용
        int fromIndex = Math.min(clientOffset, finalList.size());
        int toIndex = Math.min(clientOffset + clientSize, finalList.size());

        List<CampaignResultResponse> pagedList = new ArrayList<>();
        if (fromIndex < toIndex) {
            pagedList = finalList.subList(fromIndex, toIndex);
        }

        // 5. 응답 반환: total을 finalList의 실제 크기로 설정
        return CampaignResultListResponse.builder()
                .data(pagedList)
                .total(finalList.size()) // <-- 여기서 total을 finalList의 크기로 설정합니다.
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
                .name(listupFormDto.getName())
                .clientCompanyId(listupFormDto.getClientCompanyId())
                .clientCompanyName(listupFormDto.getClientCompanyName())
                .influencerList(influencerList)
                .build();
    }

    // 고객사 별 캠페인 목록 조회
    public List<CampaignListDTO> getCampaignsByClientCompanyId(Long clientCompanyId) {
        List<CampaignListDTO> campaigns = campaignQueryMapper.findCampaignsByClientCompanyId(clientCompanyId);
        List<Long> campaignIds = campaigns.stream()
                .map(CampaignListDTO::getCampaignId)
                .toList();

        if (!campaignIds.isEmpty()) {
            List<PipelineStepStatusDto> steps = campaignQueryMapper.findPipelineStepsByCampaignIds(campaignIds);

            for (PipelineStepStatusDto step : steps) {
                log.info(step.toString());
            }

            Map<Long, List<PipelineStepDto>> stepMap = steps.stream()
                    .collect(Collectors.groupingBy(
                            PipelineStepStatusDto::getCampaignId,
                            Collectors.mapping(
                                    s -> new PipelineStepDto(s.getStepType(), s.getCreatedAt()),
                                    Collectors.toList()
                            )
                    ));

            for (CampaignListDTO dto : campaigns) {
                List<PipelineStepDto> stepList = stepMap.getOrDefault(dto.getCampaignId(), Collections.emptyList());
                dto.setPipelineSteps(stepList);
                long completed = stepList.stream()
                        .filter(s -> s.getStepType() != null)
                        .map(s -> s.getStepType())
                        .distinct()
                        .count();

                dto.setSuccessProbability((int) ((completed / 7.0) * 100));
            }
        }

        return campaigns;
    }


    public CampaignAiResponse getCampaignWithCategory(Long clientCompanyId, String campaignName, List<Long> tags) {
        log.info("받은 쿼리 : "+ campaignName + clientCompanyId);

        List<CampaignWithCategoryDTO> responseDto = campaignQueryMapper.findCampaignWithCategory(clientCompanyId, campaignName, tags);

        for (CampaignWithCategoryDTO dto : responseDto) {
            List<CategoryDto> categoryList = campaignQueryMapper.findCategoryByCampaignId(dto.getCampaignId());
            dto.setCategoryList(categoryList);
        }

        return CampaignAiResponse.builder().campaignList(responseDto).build();
    }

    public ProposalReferenceListResponse getProposalReferenceList(Long campaignId) {
        List<ReferenceDto> proposalReferenceList = campaignQueryMapper.getReferenceList(campaignId, PipelineStepConstants.PROPOSAL);

        return ProposalReferenceListResponse.builder()
                .referenceList(proposalReferenceList)
                .build();
    }

    public ProposalDetailResponse getProposalDetail(Long pipelineId) {
        /* 폼 정보 가져오기 */
        /* 폼 가져오기 */
        ProposalFormDTO proposalFormDTO = campaignQueryMapper.findProposalDetail(pipelineId);

        /* 인풀루언서 가져오기 */
        List<InfluencerProposalInfo> influencerDto = campaignQueryMapper.findPipelineProposalInfluencer(pipelineId);

        /* 담당자 가져오기 */
        List<UserInfo> userDto = campaignQueryMapper.findPipelineUser(pipelineId);

        /* 참고 목록 가져오기 */
        List<ReferenceDto> referenceDto = campaignQueryMapper.getReferenceList(proposalFormDTO.getCampaignId(), PipelineStepConstants.LIST_UP);

        /* 의견 가져오기 */
        List<IdeaInfo> ideaDto = campaignQueryMapper.findPipeIdea(pipelineId);

        /* 조합하기 */
        ProposalFormResponse form = ProposalFormResponse.builder()
                .name(proposalFormDTO.getName())
                .clientCompanyId(proposalFormDTO.getClientCompanyId())
                .clientCompanyName(proposalFormDTO.getClientCompanyName())
                .clientManagerId(proposalFormDTO.getClientManagerId())
                .clientManagerName(proposalFormDTO.getClientManagerName())
                .pipelineStatusId(proposalFormDTO.getPipelineStatusId())
                .pipelineStatusName(proposalFormDTO.getPipelineStatusName())
                .userList(userDto)
                .campaignId(proposalFormDTO.getCampaignId())
                .campaignName(proposalFormDTO.getCampaignName())
                .requestAt(proposalFormDTO.getRequestAt())
                .presentAt(proposalFormDTO.getPresentAt())
                .startedAt(proposalFormDTO.getStartedAt())
                .endedAt(proposalFormDTO.getEndedAt())
                .influencerList(influencerDto)
                .content(proposalFormDTO.getContent())
                .notes(proposalFormDTO.getNotes())
                .build();

        /* 응답하기 */
        return ProposalDetailResponse
                .builder()
                .form(form)
                .referenceList(referenceDto)
                .ideaList(ideaDto)
                .build();
    }

    public ListupReferenceResponse getListupReferenceList(Long campaignId) {
        List<ReferenceDto> referenceDto = campaignQueryMapper.getReferenceList(campaignId, PipelineStepConstants.LIST_UP);

        return ListupReferenceResponse.builder()
                .referenceList(referenceDto).build();
    }

    public List<CommunicationHistoryResponse> getCommunicationHistoriesByClientCompany(Long clientCompanyId) {
        log.info("고객사 ID {}의 커뮤니케이션 이력 조회 시작", clientCompanyId);
        List<CommunicationHistoryResponse> histories = campaignQueryMapper.findCommunicationHistoriesByClientCompanyId(clientCompanyId);
        log.info("고객사 ID {}의 커뮤니케이션 이력 조회 완료. {}개 이력 발견", clientCompanyId, histories.size());
        return histories;
    }

    public ListupDetailResponse getRecommendInfluencerList(RecommendInfluencerRequest request) {
        // 1. 광고 상품 & 관련 인플루언서 리스트 조회
        RequestCampaign campaign = campaignQueryMapper.findProductNameByCampaignId(request.getCampaignId());
        List<Integer> influencerList = campaignQueryMapper.findInfluencerAndProduct(request, campaign.getCategoryList());

        if(influencerList.isEmpty()){
            throw new BusinessException(CampaignErrorCode.INFLUENCER_NOT_FOUND);
        }
        // 2. 각 인플루언서의 제품 리스트 구성
        List<InfluencerPrompt.InfluencerEntry> influencerEntries = new ArrayList<>();
        for (Integer influencerId : influencerList) {
            List<String> products = campaignQueryMapper.findProductByInfluencerId(influencerId);
            influencerEntries.add(new InfluencerPrompt.InfluencerEntry(influencerId, products));
        }

        // 3. JSON 요청 생성
        InfluencerPrompt promptData = new InfluencerPrompt(campaign.getProductName(), influencerEntries);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        String jsonPayload = null;
        List<Integer> matchedIds = new ArrayList<>();
        try{
            jsonPayload = mapper.writeValueAsString(promptData);


        // 4. GPT 호출
        String generateResult = chatClient.prompt()
                .system("너는 광고 제품에 가장 적합한 인플루언서를 추천해주는 비서야. 각 인플루언서는 자신이 사용하거나 리뷰한 제품 리스트를 가지고 있어.")
                .user(jsonPayload)
                .call()
                .content();

        // 5. 응답 파싱
        RecommendResult result = mapper.readValue(generateResult, RecommendResult.class);
        matchedIds = result.getBestMatchInfluencerIds();
        log.info("추천 인플루언서 ID: " + matchedIds);
        }catch (JsonProcessingException e) {
            throw new BusinessException(CampaignErrorCode.FAILED_GENERATE_RECOMMEND);
        }
        List<InfluencerInfo> responseList = new ArrayList<>();
        if(!matchedIds.isEmpty()){
            responseList = influencerQueryMapper.findInfluencerInfoList(matchedIds);
        }
        else{
            Collections.shuffle(influencerList);
            List<Integer> randomIds = influencerList.stream()
                    .limit(5)
                    .collect(Collectors.toList());
            responseList = influencerQueryMapper.findInfluencerInfoList(randomIds);
        }

        for(InfluencerInfo influencer: responseList){
            List<String> categoryList = influencerQueryMapper.findInfluencerCategories(influencer.getInfluencerId());
            influencer.setCategoryList(categoryList);
        }

        // 6. 응답 객체 생성 (예: 이 ID들을 다시 조회해도 됨)
        return ListupDetailResponse.builder()
                .influencerList(responseList)
                .build();
    }
}


