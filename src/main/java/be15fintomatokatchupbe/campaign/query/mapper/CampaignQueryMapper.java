package be15fintomatokatchupbe.campaign.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.ProposalCardDTO;
import be15fintomatokatchupbe.campaign.query.dto.request.ProposalSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalCardResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CampaignQueryMapper {
    List<ProposalCardDTO> findProposals(
            @Param("req") ProposalSearchRequest req,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("pipelineStepId") Long pipelineStepId
    );
    int countProposals(@Param("req")ProposalSearchRequest request, @Param("pipelineStepId") Long pipelineStepId);
}
