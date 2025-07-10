package be15fintomatokatchupbe.campaign.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.mapper.*;
import be15fintomatokatchupbe.campaign.query.dto.request.CampaignResultRequest;
import be15fintomatokatchupbe.campaign.query.dto.request.PipelineSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.*;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

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

    BigDecimal selectAverageExpectedProfitMargin(Long campaignId);

    String selectCampaignNotes(Long campaignId);

    List<User> selectCampaignUserList(@Param("clientCompanyId") Long clientCompanyId);

    List<Long> selectCampaignCategoryList(@Param("campaignId") Long campaignId);

    List<PipelineTimelineDto> selectPipelineTimeline(@Param("campaignId") Long campaignId);

    Long selectTotalExpectedRevenue(Long campaignId);

    List<CampaignListResponse> findPagedCampaigns(@Param("limit") int limit, @Param("offset") int offset);

    List<PipelineStepStatusDto> findPipelineStepsByCampaignIds(@Param("campaignIds") List<Long> campaignIds);

    List<CampaignSimpleDto> findCampaignList(String keyword, Long clientCompanyId);

    Long findTotalAdPrice(Long pipelineId);

    List<ReferenceDto> getReferenceList(Long campaignId, Long pipelineStepId);

    List<CampaignResultResponse> findCampaignResultList(
            @Param("request") CampaignResultRequest request, // request 객체도 명시
            @Param("offset") int offset,                     // offset 파라미터 추가 및 @Param
            @Param("size") int size,                         // size 파라미터 추가 및 @Param
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder
    );

    int countCampaignResultList(
            @Param("request") CampaignResultRequest request // count 쿼리도 request 객체 명시
    );

    // 고객사 ID로 캠페인 목록 조회
    List<CampaignListResponse> findCampaignsByClientCompanyId(@Param("clientCompanyId") Long clientCompanyId);

}
