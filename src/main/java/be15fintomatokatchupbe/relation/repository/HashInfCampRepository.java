package be15fintomatokatchupbe.relation.repository;

import be15fintomatokatchupbe.relation.domain.HashtagInfluencerCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashInfCampRepository extends JpaRepository<HashtagInfluencerCampaign, Long> {
    List<HashtagInfluencerCampaign> findByCategoryInfluencerId(Long campaignId);

    List<HashtagInfluencerCampaign> findByCampaignId(Long campaignId);
}
