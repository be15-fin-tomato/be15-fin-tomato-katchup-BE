package be15fintomatokatchupbe.campaign.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.mapper.*;
import be15fintomatokatchupbe.campaign.query.dto.request.CampaignResultRequest;
import be15fintomatokatchupbe.campaign.query.dto.request.ContractListRequest;
import be15fintomatokatchupbe.campaign.query.dto.request.PipelineSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.*;
import be15fintomatokatchupbe.influencer.query.dto.response.CampaignRecord;
import be15fintomatokatchupbe.influencer.query.dto.response.CategoryDto;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface CampaignQueryMapper {
    List<ProposalCardDTO> findPipelineList(
            @Param("req") PipelineSearchRequest req,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("pipelineStepId") Long pipelineStepId
    );

    List<ListupCardDTO> findListupList(
            @Param("req") PipelineSearchRequest req,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("pipelineStepId") Long pipelineStepId
    );

    List<QuotationCardDTO> findQuotationList(
            @Param("req") PipelineSearchRequest req,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("pipelineStepId") Long pipelineStepId
    );

    int countListup(@Param("req")PipelineSearchRequest request, @Param("pipelineStepId") Long pipelineStepId);

    int countPipeline(@Param("req")PipelineSearchRequest request, @Param("pipelineStepId") Long pipelineStepId);

    List<ContractCardDTO> findContractList(
            @Param("req") PipelineSearchRequest req,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("pipelineStepId") Long pipelineStepId
    );

    List<RevenueCardDTO> findRevenueList(
            @Param("req") PipelineSearchRequest req,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("pipelineStepId") Long pipelineStepId
    );

    ListupFormDTO findListupDetail(Long pipelineId, Long pipelineStepId);

    QuotationFormDTO findQuotationDetail(Long pipelineId, Long pipelineStepId);

    List<InfluencerInfo> findPipelineInfluencer(Long pipelineId);

    List<UserInfo> findPipelineUser(Long pipelineId);

    List<ReferenceInfo> findPipeReference(Long pipelineId, Long pipelineStepId);

    List<IdeaInfo> findPipeIdea(Long pipelineId);

    ContractFormDTO findContractDetail(Long pipelineId, Long pipelineStepId);

    List<FileInfo> findPipeFile(Long pipelineId);

    RevenueFormDTO findRevenueDetail(Long pipelineId, Long pipelineStepId);

    List<InfluencerRevenueInfo> findPipelineRevenueInfluencer(Long pipelineId);

    // 캠페인 상세
    CampaignDetailDto selectCampaignDetail(@Param("campaignId") Long campaignId);

    float selectAverageExpectedProfitMargin(Long campaignId);

    String selectCampaignNotes(Long campaignId);

    List<User> selectCampaignUserList(@Param("clientCompanyId") Long clientCompanyId);

    List<Long> selectCampaignCategoryList(@Param("campaignId") Long campaignId);

    List<PipelineTimelineDto> selectPipelineTimeline(@Param("campaignId") Long campaignId);

    Long selectTotalExpectedRevenue(Long campaignId);

    List<CampaignListsDTO> findPagedCampaigns(@Param("limit") int limit, @Param("offset") int offset, @Param("request") ContractListRequest request);

    List<PipelineStepStatusDto> findPipelineStepsByCampaignIds(@Param("campaignIds") List<Long> campaignIds);

    List<CampaignSimpleDto> findCampaignList(String keyword, Long clientCompanyId);

    Long findTotalAdPrice(Long pipelineId);

    List<ReferenceDto> getReferenceList(Long campaignId, Long pipelineStepId);

    List<CampaignResultResponse> findCampaignResultList(@Param("request") CampaignResultRequest request);

    int countCampaignResultList(
            @Param("request") CampaignResultRequest request // count 쿼리도 request 객체 명시
    );

    // 고객사 ID로 캠페인 목록 조회
    List<CampaignListDTO> findCampaignsByClientCompanyId(@Param("clientCompanyId") Long clientCompanyId);

    List<CampaignWithCategoryDTO> findCampaignWithCategory(Long clientCompanyId, String campaignName, List<Long> tags);

    List<CategoryDto> findCategoryByCampaignId(Long campaignId);

    List<PipelineStepsDto> findPipelineStepsGroupedByCampaignIds(List<Long> campaignIds);

    List<CampaignInfluencerInfo> findInfluencerInfoByPipelineIds(@Param("pipelineIds") List<Long> pipelineIds);


    int getTotalSize(@Param("request")ContractListRequest request);

    ProposalFormDTO findProposalDetail(Long pipelineId);

    List<InfluencerProposalInfo> findPipelineProposalInfluencer(Long pipelineId);

    List<CampaignRecord> findCampaignByInfluencerId(Long id);

    List<CommunicationHistoryResponse> findCommunicationHistoriesByClientCompanyId(@Param("clientCompanyId") Long clientCompanyId);
}
