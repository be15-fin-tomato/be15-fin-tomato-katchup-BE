package be15fintomatokatchupbe.campaign.query.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CampaignCommandMapper {
    List<Long> getInfluencerId(Long campaignId);
}
