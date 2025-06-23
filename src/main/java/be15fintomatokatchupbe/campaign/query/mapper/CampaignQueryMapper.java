package be15fintomatokatchupbe.campaign.query.mapper;

import be15fintomatokatchupbe.campaign.query.dto.request.ProposalSearchRequest;
import be15fintomatokatchupbe.campaign.query.dto.response.ProposalCardResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CampaignQueryMapper {
    List<ProposalCardResponse> findProposals(
            @Param("req") ProposalSearchRequest req,
            @Param("offset") int offset,
            @Param("size") int size
    );
    int countProposals(@Param("req")ProposalSearchRequest request);
}
