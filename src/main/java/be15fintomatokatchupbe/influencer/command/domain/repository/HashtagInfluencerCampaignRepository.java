package be15fintomatokatchupbe.influencer.command.domain.repository;

import be15fintomatokatchupbe.relation.domain.HashtagInfluencerCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HashtagInfluencerCampaignRepository extends JpaRepository<HashtagInfluencerCampaign, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM HashtagInfluencerCampaign h WHERE h.influencerId = :influencerId")
    void deleteByInfluencerId(Long influencerId);

    void deleteByCampaignId(Long campaignId);
}
