package be15fintomatokatchupbe.influencer.command.domain.repository;

import be15fintomatokatchupbe.relation.domain.HashtagInfluencerCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagInfluencerCampaignRepository extends JpaRepository<HashtagInfluencerCampaign, Long> {
    void deleteByInfluencerId(Long influencerId);

    void deleteByCampaignId(Long campaignId);
}
