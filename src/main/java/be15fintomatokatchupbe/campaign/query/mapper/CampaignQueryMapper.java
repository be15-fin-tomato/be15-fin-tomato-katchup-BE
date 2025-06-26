package be15fintomatokatchupbe.campaign.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.mapper.*;
import be15fintomatokatchupbe.campaign.query.dto.request.PipelineSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.IdeaInfo;
import be15fintomatokatchupbe.campaign.query.dto.response.InfluencerInfo;
import be15fintomatokatchupbe.campaign.query.dto.response.ReferenceInfo;
import be15fintomatokatchupbe.campaign.query.dto.response.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CampaignQueryMapper {
    List<ProposalCardDTO> findPipelineList(
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

    QuotationFormDTO findQuotationDetail(Long pipelineId, Long pipelineStepId);

    List<InfluencerInfo> findPipelineInfluencer(Long pipelineId);

    List<UserInfo> findPipelineUser(Long pipelineId);

    List<ReferenceInfo> findPipeReference(Long pipelineId, Long pipelineStepId);

    List<IdeaInfo> findPipeIdea(Long pipelineId);
}
